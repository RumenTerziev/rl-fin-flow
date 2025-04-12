package bg.lrsoft.rlfinflow.testconfig;

import bg.lrsoft.rlfinflow.config.AppConfig;
import bg.lrsoft.rlfinflow.security.SecurityConfig;
import bg.lrsoft.rlfinflow.service.RestService;
import bg.lrsoft.rlfinflow.service.impl.RestRequestService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
@Import(value = {SecurityConfig.class, AppConfig.class})
public class TestConfig {

    @Bean
    @Primary
    public RestService testRestService() {
        return mock(RestRequestService.class);
    }

    @Bean
    @Primary
    public ChatClient.Builder chatClientBuilder() {
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        when(builder.build()).thenReturn(mock(ChatClient.class));
        return builder;
    }

    @Bean
    @Primary
    public ChatClient chatClient() {
        ChatClient client = mock(ChatClient.class);
        String mockedContent = "Test response from mocked chat client";
        ChatClient.CallResponseSpec callResponseSpec = mock(ChatClient.CallResponseSpec.class);
        when(callResponseSpec.content()).thenReturn(mockedContent);
        ChatClient.ChatClientRequestSpec chatClientRequestSpec = mock(ChatClient.ChatClientRequestSpec.class);
        when(chatClientRequestSpec.call()).thenReturn(callResponseSpec);
        when(client.prompt(anyString())).thenReturn(chatClientRequestSpec);
        return client;
    }
}
