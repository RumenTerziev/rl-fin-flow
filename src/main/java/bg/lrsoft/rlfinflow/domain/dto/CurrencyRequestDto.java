package bg.lrsoft.rlfinflow.domain.dto;

import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;

public record CurrencyRequestDto(
        CurrencyCode baseCurrency,
        CurrencyCode currencyToConvert,
        double sumToConvert
) {
}
