package bg.lrsoft.rlfinflow.controller;

import bg.lrsoft.rlfinflow.domain.dto.ChatMessageRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.ChatMessageResponseDto;
import bg.lrsoft.rlfinflow.service.ChatAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatAiService chatAiService;

    @PostMapping("/ask-ai")
    public ChatMessageResponseDto askAi(@RequestBody ChatMessageRequestDto prompt) {
        return chatAiService.getChatResponseFromAi(prompt);
    }
}
