package bg.lrsoft.rlfinflow.domain.dto;

import java.util.Map;

public record ExchangeRespDto(

        MetaInfDto meta,

        Map<String, OpenConverterCurrencyRespDto> data
) {
}
