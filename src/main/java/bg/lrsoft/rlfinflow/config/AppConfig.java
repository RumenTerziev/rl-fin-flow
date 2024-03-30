package bg.lrsoft.rlfinflow.config;

import bg.lrsoft.rlfinflow.service.RestRequestService;
import bg.lrsoft.rlfinflow.service.RestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestService restService() {
        return new RestRequestService(new RestTemplate());
    }
}
