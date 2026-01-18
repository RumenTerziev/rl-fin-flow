package bg.lrsoft.rlfinflow.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record FinFlowUserResponseDto(

        @Schema(
                description = "Username of the user",
                example = "tommy"
        )
        String username,

        @Schema(
                description = "Email of the user",
                example = "tommyj@example.com"
        )
        String email,

        @Schema(
                description = "Picture of the user",
                example = "https://path-to-your-picture.com"
        )
        String pictureUrl
) {
}
