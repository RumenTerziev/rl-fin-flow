package bg.lrsoft.rlfinflow.testconfig;

import bg.lrsoft.rlfinflow.config.AppConfig;
import bg.lrsoft.rlfinflow.security.Oauth2LoginFailureHandler;
import bg.lrsoft.rlfinflow.security.Oauth2LoginSuccessHandler;
import bg.lrsoft.rlfinflow.security.SecurityConfig;
import bg.lrsoft.rlfinflow.service.FinFlowOauth2UserService;
import bg.lrsoft.rlfinflow.service.RestService;
import bg.lrsoft.rlfinflow.service.impl.RestRequestService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2ClientConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

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

    @Bean
    @Primary
    public Oauth2LoginSuccessHandler oauth2LoginSuccessHandler() {
        return mock(Oauth2LoginSuccessHandler.class);
    }

    @Bean
    @Primary
    public Oauth2LoginFailureHandler oauth2LoginFailureHandler() {
        return mock(Oauth2LoginFailureHandler.class);
    }

    @Bean
    @Primary
    public FinFlowOauth2UserService finFlowOath2UserService() {
        return mock(FinFlowOauth2UserService.class);
    }

    @Bean
    @Primary
    public ClientRegistrationRepository clientRegistrationRepository() {
        return mock(ClientRegistrationRepository.class);
    }
}
