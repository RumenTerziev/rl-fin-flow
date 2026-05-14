package bg.lrsoft.rlfinflow.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GDPR data portability payload. Returned by {@code GET /privacy/profile/export} and contains
 * every personal datum the application stores about the caller.
 */
@Schema(description = "User data export bundle (GDPR Art. 20 – right to data portability).")
public record UserDataExportDto(
        FinFlowUserResponseDto profile,
        List<ConsentResponseDto> consents,
        List<ConversionResponseDto> conversions,
        List<ChatMessageExportDto> chatMessages,
        LocalDateTime generatedAt
) {

    @Schema(description = "A single chat message attributed to the caller.")
    public record ChatMessageExportDto(String role, String content, LocalDateTime timestamp) {
    }
}
