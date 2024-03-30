package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyResponseDto;
import bg.lrsoft.rlfinflow.domain.dto.ExchangeRespDto;
import bg.lrsoft.rlfinflow.domain.dto.OpenConverterCurrencyRespDto;
import bg.lrsoft.rlfinflow.service.exception.NoResponseFromExternalApiWasReceived;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    @Value("${open.exchange.currency.convertor.url}")
    private String currencyConvertorUrl;

    private final RestService restService;

    public List<CurrencyResponseDto> processConvertRequest(CurrencyRequestDto requestDto) {
        String baseCurrency = requestDto.baseCurrency().toString();
        String currencies = requestDto.currencyToConvert().toString();

        ResponseEntity<ExchangeRespDto> response = restService.getForEntity(
                currencyConvertorUrl.formatted(currencies, baseCurrency),
                ExchangeRespDto.class);
        ExchangeRespDto responseBody = response.getBody();

        if (responseBody == null) {
            throw new NoResponseFromExternalApiWasReceived("No response from external sources was received!!!");
        }
        List<OpenConverterCurrencyRespDto> allCurrencyValues = responseBody.data()
                .values().stream()
                .toList();

        return getCurrencyResponseDtos(requestDto, allCurrencyValues, baseCurrency);
    }

    private static List<CurrencyResponseDto> getCurrencyResponseDtos(CurrencyRequestDto requestDto, List<OpenConverterCurrencyRespDto> allCurrencyValues, String baseCurrency) {
        List<CurrencyResponseDto> responseDtoList = new ArrayList<>();
        for (OpenConverterCurrencyRespDto currencyRespDto : allCurrencyValues) {
            double value = currencyRespDto.value();
            double sumToConvert = requestDto.sumToConvert();
            double convertedSum = sumToConvert * value;
            responseDtoList.add(new CurrencyResponseDto(
                    CurrencyCode.valueOf(baseCurrency),
                    CurrencyCode.valueOf(currencyRespDto.code()),
                    sumToConvert, convertedSum));
        }
        return responseDtoList;
    }
}