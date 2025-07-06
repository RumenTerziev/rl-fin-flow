package bg.lrsoft.rlfinflow.domain.dto;

import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents the body of the request for converting currencies")
public record CurrencyResponseDto(

        @Schema(
                description = "The base currency that was converted",
                example = "BGN"
        )
        CurrencyCode fromCurrency,

        @Schema(
                description = "The currency that the given amount was converted to",
                example = "USD"
        )
        CurrencyCode toCurrency,

        @Schema(
                description = "The sum that was converted",
                example = "20.5"
        )
        double amount,

        @Schema(
                description = "The result sum that was calculated",
                example = "11.3447"
        )
        double resultSum,

        @Schema(
        description = "The currency rate",
        example = "1.92"
        )
        double currencyRate
) {
}
