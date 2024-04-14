package bg.lrsoft.rlfinflow.testconfig;


import bg.lrsoft.rlfinflow.config.AppConfig;
import bg.lrsoft.rlfinflow.config.security.SecurityConfig;
import bg.lrsoft.rlfinflow.service.IRestService;
import bg.lrsoft.rlfinflow.service.impl.RestRequestService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@TestConfiguration
@Import(value = {AppConfig.class, SecurityConfig.class})
public class TestConfig {

    @Bean
    @Primary
    public IRestService testRestService() {
        return mock(RestRequestService.class);
    }

    @Bean
    public Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }
}
