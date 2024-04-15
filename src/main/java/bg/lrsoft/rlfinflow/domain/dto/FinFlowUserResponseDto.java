package bg.lrsoft.rlfinflow.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record FinFlowUserResponseDto(


        @Schema(
                description = "Username of the user",
                example = "tommy"
        )
        String username,

        @Schema(
                description = "First name of the user",
                example = "Tom"
        )
        String firstName,

        @Schema(
                description = "Last name of the user",
                example = "Jones"
        )
        String lastName,

        @Schema(
                description = "Email of the user",
                example = "tommyj@example.com"
        )
        String email,

        @Schema(
                description = "Phone number of the user",
                example = "088112233"
        )
        String phoneNumber
) {
}
