package bg.lrsoft.rlfinflow.security;

import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.login-redirect-props.redirect-url}")
    private String loginRedirectUrl;

    private final FinFlowUserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.debug("Authentication success, authorities: {}", authentication.getAuthorities());
        log.info("Successfully logged in as {}", authentication.getName());

        Object principalObj = authentication.getPrincipal();
        if (!(principalObj instanceof FinFlowOath2User principal)) {
            log.warn("Principal is not a SmartPrintsOAuth2User: {}", principalObj);
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        FinFlowUser user = userService.saveLoggedUser(principal);
        log.info("Logged-in user persisted: {}", user);
        response.sendRedirect("%s/applications".formatted(loginRedirectUrl));
    }
}
