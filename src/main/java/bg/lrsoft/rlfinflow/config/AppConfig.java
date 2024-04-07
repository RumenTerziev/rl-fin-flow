package bg.lrsoft.rlfinflow.config;

import bg.lrsoft.rlfinflow.service.IRestService;
import bg.lrsoft.rlfinflow.service.impl.RestRequestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean(name = "restRequestService")
    public IRestService restService() {
        return new RestRequestService(new RestTemplate());
    }
}
