package bg.lrsoft.rlfinflow.config;

import bg.lrsoft.rlfinflow.service.RestService;
import bg.lrsoft.rlfinflow.service.impl.RestRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @Bean
    public RestService restService() {
        return new RestRequestService(new RestTemplate());
    }
}
