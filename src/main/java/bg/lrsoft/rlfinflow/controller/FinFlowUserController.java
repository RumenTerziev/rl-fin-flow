package bg.lrsoft.rlfinflow.controller;

import bg.lrsoft.rlfinflow.domain.dto.ErrorPayloadDto;
import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserResponseDto;
import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserUpdateRequestDto;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "User management APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class FinFlowUserController {

    private final FinFlowUserService finFlowUserService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            schema = @Schema(
                                    contentMediaType = "application/json",
                                    implementation = FinFlowUserResponseDto.class
                            ))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            schema = @Schema(
                                    contentMediaType = "application/json",
                                    implementation = ErrorPayloadDto.class
                            ))
            )
    })
    @GetMapping("/me")
    public FinFlowUserResponseDto getMyProfile() {
        return finFlowUserService.getMyProfile();
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Success",
                    content = @Content(
                            schema = @Schema(
                                    contentMediaType = "application/json"
                            ))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            schema = @Schema(
                                    contentMediaType = "application/json",
                                    implementation = ErrorPayloadDto.class
                            ))
            )
    })
    @PutMapping("/me")
    public ResponseEntity<Object> updateProfile(@RequestBody FinFlowUserUpdateRequestDto finFlowUserUpdateRequestDto) {
        finFlowUserService.updateProfile(finFlowUserUpdateRequestDto);
        return ResponseEntity.noContent().build();

    }
}
