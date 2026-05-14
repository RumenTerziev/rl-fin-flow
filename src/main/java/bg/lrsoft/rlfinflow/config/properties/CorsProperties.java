package bg.lrsoft.rlfinflow.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * CORS configuration. The SPA lives on a different origin than the API (e.g. {@code
 * http://localhost:4200} vs {@code http://localhost:8080/api/v1}); the browser therefore
 * requires explicit {@code Access-Control-Allow-Origin} headers and {@code Allow-Credentials}
 * for the session cookie used by OAuth2 / consent persistence to flow.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

    /** Origins permitted to call the API with credentials. Keep this list as narrow as possible. */
    private List<String> allowedOrigins = List.of("http://localhost:4200");
}
