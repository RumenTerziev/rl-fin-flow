package bg.lrsoft.rlfinflow.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Represents a user registered in the fin flow application")
public record FinFlowUserImportDto(

        //ToDo fix examples
        @Schema(
                description = "Error message",
                example = "No response from external sources was received!!!"
        )
        @NotBlank
        String username,

        @Schema(
                description = "Error message",
                example = "No response from external sources was received!!!"
        )
        @NotBlank
        String password,

        @Schema(
                description = "Error message",
                example = "No response from external sources was received!!!"
        )
        @NotBlank
        String firstName,

        @Schema(
                description = "Error message",
                example = "No response from external sources was received!!!"
        )
        String lastName,

        @Schema(
                description = "Error message",
                example = "No response from external sources was received!!!"
        )
        @Email
        String email,

        @Schema(
                description = "Error message",
                example = "No response from external sources was received!!!"
        )
        String phoneNumber
) {
}
