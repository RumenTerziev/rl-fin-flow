package bg.lrsoft.rlfinflow.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Represents a user registered in the fin flow application")
public record FinFlowUserRegisterDto(

        @Schema(
                description = "Username of the user",
                example = "tommy"
        )
        @NotBlank
        String username,

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
