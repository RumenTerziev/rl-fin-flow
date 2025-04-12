package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.domain.dto.ChatMessageRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.ChatMessageResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;

@Slf4j
public class ChatAiService {

    private final ChatClient chatClient;

    public ChatAiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public ChatMessageResponseDto getChatResponseFromAi(ChatMessageRequestDto chatMessageRequestDto) {
        String message = chatMessageRequestDto.message();
        String resp = chatClient.prompt(message)
                .call()
                .content();
        log.info("Response from AI: {}", resp);
        return new ChatMessageResponseDto(resp);
    }
}
