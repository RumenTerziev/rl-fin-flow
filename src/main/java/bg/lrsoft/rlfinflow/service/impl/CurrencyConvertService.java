package bg.lrsoft.rlfinflow.service.impl;

import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyResponseDto;
import bg.lrsoft.rlfinflow.domain.dto.ExchangeRespDto;
import bg.lrsoft.rlfinflow.domain.dto.OpenConverterCurrencyRespDto;
import bg.lrsoft.rlfinflow.service.ICurrencyService;
import bg.lrsoft.rlfinflow.service.IRestService;
import bg.lrsoft.rlfinflow.service.exception.NoResponseFromExternalApiWasReceived;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyConvertService implements ICurrencyService {

    @Value("${open.exchange.currency.convertor.url}")
    private String currencyConvertorUrl;

    @Autowired
    @Qualifier("restRequestService")
    private final IRestService restService;

    public CurrencyConvertService(IRestService restService) {
        this.restService = restService;
    }

    @Override
    public List<CurrencyResponseDto> processConvertRequest(CurrencyRequestDto requestDto) {
        String baseCurrency = requestDto.baseCurrency().toString();
        String currencyToConvertTo = requestDto.currencyToConvert().toString();

        ResponseEntity<ExchangeRespDto> response = restService.getForEntity(
                resolveUrl(currencyToConvertTo, baseCurrency),
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

    public String resolveUrl(String currencyToConvertTo, String baseCurrency) {
        return currencyConvertorUrl.formatted(currencyToConvertTo, baseCurrency);
    }

    private List<CurrencyResponseDto> getCurrencyResponseDtos(CurrencyRequestDto requestDto, List<OpenConverterCurrencyRespDto> allCurrencyValues, String baseCurrency) {
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
