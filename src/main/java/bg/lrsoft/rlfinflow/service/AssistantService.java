package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.domain.dto.ChatMessageRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.ChatMessageResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AssistantService {

    private final ChatClient chatClient;

    public AssistantService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public ChatMessageResponseDto getChatResponseFromAi(ChatMessageRequestDto chatMessageRequestDto) {
        String message = chatMessageRequestDto.prompt() ;
        String resp = chatClient.prompt(message)
                .call()
                .content();
        log.info("Response from AI: {}", resp);
        return new ChatMessageResponseDto(resp);
    }
}
