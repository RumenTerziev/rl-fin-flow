package bg.lrsoft.rlfinflow.controller;

import bg.lrsoft.rlfinflow.domain.dto.ChatMessageRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.ChatMessageResponseDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyResponseDto;
import bg.lrsoft.rlfinflow.domain.dto.ErrorPayloadDto;
import bg.lrsoft.rlfinflow.service.AssistantService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/assistant")
public class ChatController {

    private final AssistantService assistantService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CurrencyResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorPayloadDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorPayloadDto.class)
                    )
            )
    })
    @PostMapping("/chat")
    public ChatMessageResponseDto chatWithAi(@RequestBody ChatMessageRequestDto prompt) {
        return assistantService.getChatResponseFromAi(prompt);
    }
}
