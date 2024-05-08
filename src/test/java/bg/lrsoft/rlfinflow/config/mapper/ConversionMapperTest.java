package bg.lrsoft.rlfinflow.config.mapper;

import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;
import bg.lrsoft.rlfinflow.domain.dto.ConversionResponseDto;
import bg.lrsoft.rlfinflow.domain.model.Conversion;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

import static bg.lrsoft.rlfinflow.domain.constant.CurrencyCode.BGN;
import static bg.lrsoft.rlfinflow.domain.constant.CurrencyCode.EUR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ConversionMapperTest {

    private final ConversionMapper conversionMapper = new ConversionMapperImpl();

    @Test
    void testMapToResponseDto_whenGivenValidEntity_shouldMapToResponseDto() {
        //Given
        String loggedUsername = "johnd";
        CurrencyCode baseCurrency = EUR;
        CurrencyCode currencyToConvertTo = BGN;
        double sumToConvert = 10.0;
        double resultSum = 20.0;

        Conversion conversion = new Conversion(loggedUsername, baseCurrency, currencyToConvertTo, sumToConvert, resultSum);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String expectedCreatedAt = dateTimeFormatter.format(conversion.getCreatedAt());

        //When
        ConversionResponseDto result = conversionMapper.mapToResponseDto(conversion);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.baseCurrency()).isEqualTo(baseCurrency);
        assertThat(result.currencyToConvertTo()).isEqualTo(currencyToConvertTo);
        assertThat(result.sumToConvert()).isEqualTo(sumToConvert);
        assertThat(result.resultSum()).isEqualTo(resultSum);
        assertThat(result.createdAt()).isEqualTo(expectedCreatedAt);
    }
}
