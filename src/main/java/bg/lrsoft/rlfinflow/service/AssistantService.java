package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.domain.dto.ChatMessageRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.ChatMessageResponseDto;
import bg.lrsoft.rlfinflow.domain.model.ChatMessage;
import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class AssistantService {

    private final ChatClient chatClient;
    private final FinFlowUserService finFlowUserService;
    private final MessageRepository messageRepository;

    public AssistantService(ChatClient.Builder chatClientBuilder,
                            FinFlowUserService finFlowUserService,
                            MessageRepository messageRepository) {
        this.chatClient = chatClientBuilder.build();
        this.finFlowUserService = finFlowUserService;
        this.messageRepository = messageRepository;
    }

    public ChatMessageResponseDto getChatResponseFromAi(ChatMessageRequestDto chatMessageRequestDto) {
        FinFlowUser user = finFlowUserService.getAuthenticatedUser();
        String username = user.getUsername();
        String userInput = chatMessageRequestDto.prompt();

        saveMessage(username, "user", userInput);

        List<ChatMessage> history = messageRepository
                .findTop20ByUsernameOrderByTimestampDesc(username);
        Collections.reverse(history); // Oldest first

        List<Message> aiMessages = new ArrayList<>();
        for (ChatMessage msg : history) {
            if ("user".equalsIgnoreCase(msg.getRole())) {
                aiMessages.add(new UserMessage(msg.getContent()));
            } else if ("assistant".equalsIgnoreCase(msg.getRole())) {
                aiMessages.add(new AssistantMessage(msg.getContent()));
            }
        }

        aiMessages.add(new UserMessage(userInput));

        String aiReply = chatClient.prompt()
                .messages(aiMessages)
                .call()
                .content();

        saveMessage(username, "assistant", aiReply);

        return new ChatMessageResponseDto(aiReply);
    }

    private void saveMessage(String username, String role, String content) {
        ChatMessage message = ChatMessage.builder()
                .username(username)
                .role(role)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();
        messageRepository.save(message);
    }
}
