package bg.lrsoft.rlfinflow.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Public-facing summary of a stored consent record (no PII apart from email).")
public record ConsentResponseDto(
        String policyVersion,
        LocalDateTime acceptedAt
) {
}
