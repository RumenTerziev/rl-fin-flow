package bg.lrsoft.rlfinflow.domain.dto;

import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;

public record ConversionResponseDto(

        CurrencyCode fromCurrency,

        CurrencyCode toCurrency,

        double amount,

        double resultSum,

        double currencyRate,

        String createdAt
) {
}
