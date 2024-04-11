package bg.lrsoft.rlfinflow.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Represents errors")

public record ErrorPayloadDto(
        @Schema(
                description = "Error message",
                example = "No response from external sources was received!!!"
        )
        String message,

        @Schema(
                description = "When the error occurred"
        )
        LocalDateTime timestamp
) {
}
