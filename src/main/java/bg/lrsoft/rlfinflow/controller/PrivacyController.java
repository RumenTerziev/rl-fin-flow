package bg.lrsoft.rlfinflow.controller;

import bg.lrsoft.rlfinflow.domain.dto.ConsentRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.UserDataExportDto;
import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import bg.lrsoft.rlfinflow.service.PrivacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * GDPR endpoints — consent capture, data export (Art. 20), data erasure (Art. 17).
 */
@Slf4j
@Tag(name = "Privacy", description = "GDPR self-service endpoints")
@RestController
@RequestMapping("/privacy")
@RequiredArgsConstructor
public class PrivacyController {

    private final PrivacyService privacyService;
    private final FinFlowUserService finFlowUserService;

    /**
     * Anonymous-friendly endpoint: stash the user's consent to the latest privacy policy in
     * the HTTP session before they continue to the OAuth provider. The actual {@code ConsentRecord}
     * row is written by {@code Oauth2LoginSuccessHandler} once the email has been verified.
     */
    @Operation(summary = "Record GDPR consent prior to OAuth login")
    @PostMapping("/consent")
    public ResponseEntity<Map<String, String>> recordConsent(@Valid @RequestBody ConsentRequestDto body,
                                                             HttpServletRequest request) {
        String expected = privacyService.currentPolicyVersion();
        if (!expected.equals(body.policyVersion())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "policy-version-mismatch", "expected", expected));
        }
        privacyService.stashPendingConsent(body.policyVersion(), request);
        return ResponseEntity.accepted().body(Map.of("policyVersion", expected));
    }

    @Operation(summary = "Return the current privacy policy version")
    @GetMapping("/policy-version")
    public Map<String, String> currentPolicyVersion() {
        return Map.of("policyVersion", privacyService.currentPolicyVersion());
    }

    @Operation(summary = "Export all personal data stored for the caller (GDPR Art. 20)")
    @GetMapping("/profile/export")
    public UserDataExportDto exportMine() {
        FinFlowUser user = finFlowUserService.getAuthenticatedFinFlowUser();
        return privacyService.exportFor(user);
    }

    /**
     * Permanent deletion of the caller's account and all linked records. Invalidates the
     * session to make sure the now-deleted principal can no longer act.
     */
    @Operation(summary = "Erase all personal data and the account (GDPR Art. 17)")
    @DeleteMapping("/profile")
    public ResponseEntity<Void> eraseMine(HttpServletRequest request) {
        FinFlowUser user = finFlowUserService.getAuthenticatedFinFlowUser();
        privacyService.eraseFor(user);
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }
}
