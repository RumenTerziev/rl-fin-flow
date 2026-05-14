package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.config.mapper.ConversionMapper;
import bg.lrsoft.rlfinflow.config.mapper.FinFlowUserMapper;
import bg.lrsoft.rlfinflow.config.properties.GdprProperties;
import bg.lrsoft.rlfinflow.domain.dto.ConsentResponseDto;
import bg.lrsoft.rlfinflow.domain.dto.UserDataExportDto;
import bg.lrsoft.rlfinflow.domain.model.ConsentRecord;
import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.repository.ConsentRepository;
import bg.lrsoft.rlfinflow.repository.ConversionRepository;
import bg.lrsoft.rlfinflow.repository.FinFlowUserRepository;
import bg.lrsoft.rlfinflow.repository.MessageRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

/**
 * Core GDPR plumbing: capturing consent, exporting personal data and erasing it.
 *
 * <p>Consent is captured in two phases (see option A in the design notes):
 * <ol>
 *     <li>The anonymous SPA submits {@code POST /privacy/consent} before redirecting to
 *         Google. This service stashes a pending consent payload in the HTTP session.</li>
 *     <li>{@code Oauth2LoginSuccessHandler} later calls {@link #finalizePendingConsent} which
 *         persists a {@link ConsentRecord} bound to the verified email.</li>
 * </ol>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PrivacyService {

    /** HttpSession attribute used to bridge the anonymous consent submission with the OAuth callback. */
    public static final String PENDING_CONSENT_ATTR = "rl-fin-flow.pendingConsent";

    private final GdprProperties gdprProperties;
    private final ConsentRepository consentRepository;
    private final FinFlowUserRepository userRepository;
    private final ConversionRepository conversionRepository;
    private final MessageRepository messageRepository;
    private final ConversionMapper conversionMapper;
    private final FinFlowUserMapper userMapper;

    public String currentPolicyVersion() {
        return gdprProperties.getPolicyVersion();
    }

    /** Records an anonymous, pre-login consent in the HTTP session. */
    public PendingConsent stashPendingConsent(String policyVersion, HttpServletRequest request) {
        PendingConsent pending = new PendingConsent(
                policyVersion,
                LocalDateTime.now(),
                hashIp(request.getRemoteAddr()),
                truncate(request.getHeader("User-Agent"), 512)
        );
        var session = request.getSession(true);
        session.setAttribute(PENDING_CONSENT_ATTR, pending);
        log.info("Stashed pending consent (policyVersion={}, sessionId={})",
                policyVersion, session.getId());
        return pending;
    }

    /**
     * Persists the consent that was stashed pre-login (if any) under the verified email.
     * Returns {@code true} when the user has a valid consent record for the current policy
     * version after the call (either previously recorded or now just persisted).
     */
    @Transactional
    public boolean finalizePendingConsent(String email, HttpServletRequest request) {
        String policyVersion = currentPolicyVersion();

        Optional<ConsentRecord> existing = consentRepository
                .findTopByEmailAndPolicyVersionOrderByAcceptedAtDesc(email, policyVersion);
        if (existing.isPresent()) {
            log.info("Existing consent record found for {} (policy {})", email, policyVersion);
            clearPending(request);
            return true;
        }

        var currentSession = request.getSession(false);
        Object stashed = currentSession == null ? null : currentSession.getAttribute(PENDING_CONSENT_ATTR);
        log.info("Finalizing consent for {}: sessionId={}, stashedPresent={}",
                email,
                currentSession == null ? "<none>" : currentSession.getId(),
                stashed != null);

        if (!(stashed instanceof PendingConsent pending) || !policyVersion.equals(pending.policyVersion())) {
            log.warn("Login for {} blocked: no current-version consent record found", email);
            return false;
        }

        ConsentRecord record = new ConsentRecord(
                email,
                pending.policyVersion(),
                pending.acceptedAt(),
                pending.ipHash(),
                pending.userAgent()
        );
        consentRepository.save(record);
        clearPending(request);
        log.info("Persisted consent for {} (policy {})", email, policyVersion);
        return true;
    }

    public UserDataExportDto exportFor(FinFlowUser user) {
        List<ConsentResponseDto> consents = consentRepository.findAll().stream()
                .filter(c -> user.getEmail().equals(c.getEmail()))
                .map(c -> new ConsentResponseDto(c.getPolicyVersion(), c.getAcceptedAt()))
                .toList();

        List<bg.lrsoft.rlfinflow.domain.dto.ConversionResponseDto> conversions = conversionRepository
                .findAllByLoggedUsername(user.getEmail()).stream()
                .map(conversionMapper::mapToResponseDto)
                .toList();

        List<UserDataExportDto.ChatMessageExportDto> chats = messageRepository
                .findAllByUsernameOrderByTimestampAsc(user.getEmail()).stream()
                .map(m -> new UserDataExportDto.ChatMessageExportDto(m.getRole(), m.getContent(), m.getTimestamp()))
                .toList();

        return new UserDataExportDto(
                userMapper.mapToResponseDto(user),
                consents,
                conversions,
                chats,
                LocalDateTime.now()
        );
    }

    /**
     * GDPR Art. 17 – right to erasure. Removes every datum tied to the email and the user row
     * itself, in a single transaction.
     */
    @Transactional
    public void eraseFor(FinFlowUser user) {
        String email = user.getEmail();
        log.info("Erasing all data for {}", email);
        messageRepository.deleteByUsername(email);
        conversionRepository.deleteByLoggedUsername(email);
        consentRepository.deleteByEmail(email);
        userRepository.deleteByEmail(email);
    }

    private void clearPending(HttpServletRequest request) {
        if (request.getSession(false) != null) {
            request.getSession(false).removeAttribute(PENDING_CONSENT_ATTR);
        }
    }

    private static String truncate(String value, int max) {
        if (value == null) return null;
        return value.length() <= max ? value : value.substring(0, max);
    }

    /** Salt-free SHA-256 of the IP. Stored only as audit, never as PII. */
    private static String hashIp(String ip) {
        if (ip == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(ip.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /** Pre-login consent placeholder, stored as a session attribute. */
    public record PendingConsent(String policyVersion, LocalDateTime acceptedAt, String ipHash, String userAgent)
            implements java.io.Serializable {
    }
}
