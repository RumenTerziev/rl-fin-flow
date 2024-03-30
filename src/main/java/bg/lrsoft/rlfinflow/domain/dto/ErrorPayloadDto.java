package bg.lrsoft.rlfinflow.domain.dto;

import java.time.LocalDateTime;

public record ErrorPayloadDto(
        String message,
        LocalDateTime timestamp
) {
}
