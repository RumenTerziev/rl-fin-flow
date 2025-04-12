package bg.lrsoft.rlfinflow.testconfig;

import bg.lrsoft.rlfinflow.config.AppConfig;
import bg.lrsoft.rlfinflow.security.SecurityConfig;
import bg.lrsoft.rlfinflow.service.ChatAiService;
import bg.lrsoft.rlfinflow.service.RestService;
import bg.lrsoft.rlfinflow.service.impl.RestRequestService;
import org.springframework.ai.chat.client.ChatClient;
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
    public RestService testRestService() {
        return mock(RestRequestService.class);
    }

    @Bean
    @Primary
    public ChatAiService testChatAiService() {
        return new ChatAiService(mock(ChatClient.Builder.class));
    }
}
