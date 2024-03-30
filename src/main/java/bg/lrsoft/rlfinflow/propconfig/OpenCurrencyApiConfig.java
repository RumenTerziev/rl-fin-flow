package bg.lrsoft.rlfinflow.propconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "open.exchange.currency.convertor")
public class OpenCurrencyApiConfig {

    private String url;
}
