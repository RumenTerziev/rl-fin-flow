package bg.lrsoft.rlfinflow.domain.dto;

import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;

public record CurrencyResponseDto(
        CurrencyCode baseCurrency,
        CurrencyCode currency,
        double sumToConvert,
        double resultSum
) {
    @Override
    public String toString() {
        return "%.4f %s are %4f %s! Thank you for using our open API!";
    }
}
