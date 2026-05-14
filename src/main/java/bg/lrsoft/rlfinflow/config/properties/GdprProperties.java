package bg.lrsoft.rlfinflow.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * GDPR configuration: the version of the current privacy policy. When the policy changes
 * (substantive update), bump this version and any existing consent records will be treated
 * as outdated, forcing users to re-accept on next login.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.gdpr")
public class GdprProperties {

    private String policyVersion = "1";
}
