package bg.lrsoft.rlfinflow.controller;

import bg.lrsoft.rlfinflow.domain.dto.ErrorPayloadDto;
import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserRegisterDto;
import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserResponseDto;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Auth", description = "Authentication APIs")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String GOOGLE_LOGIN_ENDPOINT = "/api/v1/oauth2/authorization/google";
    private final FinFlowUserService finFlowUserService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Success"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            schema = @Schema(
                                    contentMediaType = "application/json",
                                    implementation = ErrorPayloadDto.class))
            )
    })
    @PostMapping("/register")
    public FinFlowUserResponseDto register(@Valid @RequestBody FinFlowUserRegisterDto finFlowUserImportDto) {
        return finFlowUserService.registerUser(finFlowUserImportDto);
    }

    @GetMapping("/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        response.sendRedirect(GOOGLE_LOGIN_ENDPOINT);
    }
}
