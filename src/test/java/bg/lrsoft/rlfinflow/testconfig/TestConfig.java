package bg.lrsoft.rlfinflow.testconfig;


import bg.lrsoft.rlfinflow.config.AppConfig;
import bg.lrsoft.rlfinflow.repository.FinFlowUserRepository;
import bg.lrsoft.rlfinflow.security.SecurityConfig;
import bg.lrsoft.rlfinflow.service.BasicUserDetailsService;
import bg.lrsoft.rlfinflow.service.IRestService;
import bg.lrsoft.rlfinflow.service.impl.RestRequestService;
import io.cucumber.spring.ScenarioScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.mockito.Mockito.mock;

@TestConfiguration
@Import(value = {AppConfig.class, SecurityConfig.class})
public class TestConfig {

    @Bean(name = "testRestRequestService")
    @ScenarioScope
    public IRestService getTestRestService() {
        return mock(RestRequestService.class);
    }

    @Bean
    public BasicUserDetailsService basicUserDetailsService(FinFlowUserRepository finFlowUserRepository) {
        return new BasicUserDetailsService(finFlowUserRepository);
    }

    @Bean
    public FinFlowUserRepository userRepository() {
        return new FinFlowUserRepository();
    }
}