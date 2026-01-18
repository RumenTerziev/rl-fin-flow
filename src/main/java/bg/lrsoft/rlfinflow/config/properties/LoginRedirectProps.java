package bg.lrsoft.rlfinflow.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.login-redirect-props")
public class LoginRedirectProps {

    private String redirectUrl;
}