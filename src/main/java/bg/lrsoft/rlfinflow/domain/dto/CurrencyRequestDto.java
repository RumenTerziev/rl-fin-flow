package bg.lrsoft.rlfinflow.domain.dto;

import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;
import io.swagger.v3.oas.annotations.media.Schema;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "Represents the body of the request for converting currencies")
public record CurrencyRequestDto(

        @Schema(
                description = "The base currency that would be converted",
                example = "BGN",
                requiredMode = REQUIRED
        )
        CurrencyCode fromCurrency,

        @Schema(
                description = "The currency that you would like to convert to",
                example = "USD",
                requiredMode = REQUIRED
        )
        CurrencyCode toCurrency,

        @Schema(
                description = "The sum that would be converted",
                example = "20.5",
                requiredMode = REQUIRED
        )
        double amount
) {
}
