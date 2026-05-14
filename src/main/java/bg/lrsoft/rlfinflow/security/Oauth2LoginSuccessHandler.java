package bg.lrsoft.rlfinflow.security;

import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import bg.lrsoft.rlfinflow.service.PrivacyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.login-redirect-props.redirect-url}")
    private String loginRedirectUrl;

    private final FinFlowUserService userService;
    private final PrivacyService privacyService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.debug("Authentication success, authorities: {}", authentication.getAuthorities());
        log.info("Successfully logged in as {}", authentication.getName());

        Object principalObj = authentication.getPrincipal();
        if (!(principalObj instanceof FinFlowOath2User principal)) {
            log.warn("Principal is not a FinFlowOath2User: {}", principalObj.getClass());
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        FinFlowUser user = userService.saveLoggedUser(principal);
        log.info("Logged-in user persisted: {}", user != null ? user.getEmail() : "null");

        // GDPR gate: refuse to start a session if the user has not consented to the current policy.
        boolean hasConsent = privacyService.finalizePendingConsent(principal.getEmail(), request);
        if (!hasConsent) {
            log.warn("Consent missing for {} — invalidating session and bouncing to login", principal.getEmail());
            if (request.getSession(false) != null) {
                request.getSession(false).invalidate();
            }
            SecurityContextHolder.clearContext();
            String target = "%s/login?consentRequired=true&policyVersion=%s".formatted(
                    loginRedirectUrl,
                    URLEncoder.encode(privacyService.currentPolicyVersion(), StandardCharsets.UTF_8));
            response.sendRedirect(target);
            return;
        }

        response.sendRedirect("%s/profile".formatted(loginRedirectUrl));
    }
}
