package bg.lrsoft.rlfinflow.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.security-props")
public class AppSecurityProps {

    private String adminName;
    private String adminUserEmail;
    private String adminPictureUrl;
}