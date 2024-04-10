package bg.lrsoft.rlfinflow.domain.dto;

public record LoginFailedErrorPayload(
        String error,
        String message
) {
}
