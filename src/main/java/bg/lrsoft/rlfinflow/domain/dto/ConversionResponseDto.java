package bg.lrsoft.rlfinflow.domain.dto;

import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;

public record ConversionResponseDto(

        CurrencyCode baseCurrency,

        CurrencyCode currencyToConvertTo,

        double sumToConvert,

        double resultSum,

        double currencyRate,

        String createdAt
) {
}
