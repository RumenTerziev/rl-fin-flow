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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "User management APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class FinFlowUserController {

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

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            schema = @Schema(
                                    contentMediaType = "application/json",
                                    implementation = FinFlowUserResponseDto.class))
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
    @GetMapping("/profile")
    public FinFlowUserResponseDto getMyProfile() {
        return finFlowUserService.getMyProfile();
    }
}
