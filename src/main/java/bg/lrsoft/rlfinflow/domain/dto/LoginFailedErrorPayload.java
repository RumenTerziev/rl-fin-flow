package bg.lrsoft.rlfinflow.domain.dto;

import java.time.LocalDateTime;

public record LoginFailedErrorPayload(
        String error,
        String message,
        LocalDateTime timestamp
) {
}
