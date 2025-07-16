package bg.lrsoft.rlfinflow.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Represents the updated profile of the user")
public record FinFlowUserUpdateRequestDto(

        @Schema(
                description = "Password of the user",
                example = "asd123dsa"
        )
        @NotBlank
        String password,

        @Schema(
                description = "Email of the user",
                example = "tommyj@example.com"
        )
        @Email
        String email
) {
}
