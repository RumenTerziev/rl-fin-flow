package bg.lrsoft.rlfinflow.domain.dto;

import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;
import io.swagger.v3.oas.annotations.media.Schema;

public record CurrencyResponseDto(
        @Schema(
                description = "The base currency that was converted",
                example = "BGN"
        )
        CurrencyCode baseCurrency,
        @Schema(
                description = "The currency that the given amount was converted to",
                example = "USD"
        )
        CurrencyCode currency,
        @Schema(
                description = "The sum that was converted",
                example = "20.5"
        )
        double sumToConvert,
        @Schema(
                description = "The result sum that was calculated",
                example = "11.3447"
        )
        double resultSum
) {
}
