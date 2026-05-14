package bg.lrsoft.rlfinflow.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Body submitted by the SPA to record GDPR consent before initiating OAuth login.")
public record ConsentRequestDto(
        @Schema(description = "Version string of the policy that was accepted (must match server policy).",
                example = "1")
        @NotBlank
        String policyVersion
) {
}
