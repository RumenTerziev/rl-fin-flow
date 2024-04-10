package bg.lrsoft.rlfinflow.security;

import bg.lrsoft.rlfinflow.domain.dto.LoginFailedErrorPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class LoginFailureHandler implements AuthenticationFailureHandler, AuthenticationEntryPoint {

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        setHttpServletResponse(response, authException.getMessage());
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        setHttpServletResponse(response, exception.getMessage());
    }

    private void setHttpServletResponse(HttpServletResponse response, String exceptionMsg) throws IOException {
        response.setStatus(SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);

        String errorMsg = "Error during authentication. Bad Credentials.";
        LoginFailedErrorPayload exceptionDto = new LoginFailedErrorPayload(errorMsg, exceptionMsg);
        String responseJson = mapper.writeValueAsString(exceptionDto);

        response.getWriter().write(responseJson);
    }
}
