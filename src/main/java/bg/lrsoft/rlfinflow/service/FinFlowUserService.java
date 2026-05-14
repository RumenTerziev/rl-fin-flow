package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.config.mapper.FinFlowUserMapper;
import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserResponseDto;
import bg.lrsoft.rlfinflow.domain.exception.NoUserLoggedInException;
import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.repository.FinFlowUserRepository;
import bg.lrsoft.rlfinflow.security.FinFlowOath2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User-management service.
 *
 * <p>Password-based authentication was removed; this service is responsible only for OAuth2
 * principals: persisting them on first login, returning the current profile, and exposing the
 * authenticated {@link FinFlowUser} for downstream services (chat, GDPR).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FinFlowUserService {

    private final FinFlowUserRepository finFlowUserRepository;
    private final FinFlowUserMapper mapper;

    public FinFlowUserResponseDto getMyProfile() {
        FinFlowUser user = getAuthenticatedFinFlowUser();
        log.info("Returning profile for authenticated user: {}", user);
        return mapper.mapToResponseDto(user);
    }

    /**
     * Returns the {@link FinFlowUser} corresponding to the currently authenticated OAuth2
     * principal. Throws {@link NoUserLoggedInException} when nobody is authenticated.
     */
    public FinFlowUser getAuthenticatedFinFlowUser() {
        FinFlowOath2User principal = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(FinFlowOath2User.class::isInstance)
                .map(FinFlowOath2User.class::cast)
                .orElseThrow(NoUserLoggedInException::new);

        return finFlowUserRepository.findByEmail(principal.getEmail())
                .orElseThrow(NoUserLoggedInException::new);
    }

    /**
     * Persists the OAuth2 principal on first login, or updates the existing row.
     * Returns {@code null} when no principal is supplied (defensive).
     */
    public FinFlowUser saveLoggedUser(@AuthenticationPrincipal FinFlowOath2User principal) {
        if (principal == null) {
            log.debug("No authenticated user to save, returning null");
            return null;
        }
        log.info("Persisting user: {}", principal.getEmail());
        return finFlowUserRepository.findByEmail(principal.getEmail())
                .map(existing -> {
                    existing.updateFromPrincipal(principal);
                    return finFlowUserRepository.save(existing);
                })
                .orElseGet(() -> finFlowUserRepository.save(mapper.fromPrincipal(principal)));
    }
}
