package bg.lrsoft.rlfinflow.service.impl;

import bg.lrsoft.rlfinflow.config.mapper.ConversionMapper;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyResponseDto;
import bg.lrsoft.rlfinflow.domain.dto.ExchangeRespDto;
import bg.lrsoft.rlfinflow.domain.dto.OpenConverterCurrencyRespDto;
import bg.lrsoft.rlfinflow.domain.exception.NoResponseFromExternalApiWasReceived;
import bg.lrsoft.rlfinflow.repository.ConversionRepository;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import bg.lrsoft.rlfinflow.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("OpenApiRateCurrencyService")
public class OpenApiRateCurrencyService extends AbstractCurrencyService {

    @Value("${open.exchange.currency.convertor.url}")
    private String currencyConvertorUrl;

    private final RestService restService;

    @Autowired
    public OpenApiRateCurrencyService(FinFlowUserService finFlowUserService,
                                      ConversionRepository conversionRepository,
                                      ConversionMapper conversionMapper,
                                      RestService restService) {
        super(finFlowUserService, conversionRepository, conversionMapper);
        this.restService = restService;
    }

    @Override
    public CurrencyResponseDto processConvertRequest(CurrencyRequestDto requestDto) {
        String fromCurrency = requestDto.fromCurrency().toString();
        String toCurrency = requestDto.toCurrency().toString();
        ExchangeRespDto responseBody = retrieveCurrencyResponseDto(fromCurrency, toCurrency);
        List<OpenConverterCurrencyRespDto> allCurrencyValues = responseBody.data()
                .values().stream()
                .toList();

        CurrencyResponseDto currencyResponseDto = mapResponseFromOpenApi(requestDto, allCurrencyValues, fromCurrency);
        conversionRepository.save(getConversionFromRespDto(currencyResponseDto));
        return currencyResponseDto;
    }

    private ExchangeRespDto retrieveCurrencyResponseDto(String fromCurrency, String toCurrency) {
        ResponseEntity<ExchangeRespDto> response = restService.getForEntity(
                currencyConvertorUrl.formatted(toCurrency, fromCurrency),
                ExchangeRespDto.class);
        ExchangeRespDto responseBody = response.getBody();
        if (responseBody == null) {
            throw new NoResponseFromExternalApiWasReceived("No response from external sources was received!");
        }
        return responseBody;
    }

    private CurrencyResponseDto mapResponseFromOpenApi(CurrencyRequestDto requestDto, List<OpenConverterCurrencyRespDto> allCurrencyValues, String fromCurrency) {
        for (OpenConverterCurrencyRespDto currencyRespDto : allCurrencyValues) {
            if (currencyRespDto.code().equals(requestDto.toCurrency().toString())) {
                double value = currencyRespDto.value();
                return getCurrencyResponseDto(fromCurrency, currencyRespDto.code(), requestDto.amount(), value);
            }
        }
        throw new NoResponseFromExternalApiWasReceived("No response from external sources for the desired currency was received!");
    }
}
