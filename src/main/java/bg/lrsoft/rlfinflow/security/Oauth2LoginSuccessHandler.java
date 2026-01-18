package bg.lrsoft.rlfinflow.security;

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


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.debug("authentication success, {}", authentication.getAuthorities());
        log.info("Successfully logged in as {}", authentication.getName());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.sendRedirect("%s/profile".formatted(loginRedirectUrl));
    }
}
