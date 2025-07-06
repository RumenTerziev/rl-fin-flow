package bg.lrsoft.rlfinflow.service.impl;

import bg.lrsoft.rlfinflow.config.mapper.ConversionMapper;
import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;
import bg.lrsoft.rlfinflow.domain.dto.*;
import bg.lrsoft.rlfinflow.domain.exception.NoResponseFromExternalApiWasReceived;
import bg.lrsoft.rlfinflow.domain.model.Conversion;
import bg.lrsoft.rlfinflow.domain.model.PageResult;
import bg.lrsoft.rlfinflow.repository.ConversionRepository;
import bg.lrsoft.rlfinflow.service.CurrencyService;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import bg.lrsoft.rlfinflow.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyConvertService implements CurrencyService {

    @Value("${open.exchange.currency.convertor.url}")
    private String currencyConvertorUrl;

    private final FinFlowUserService finFlowUserService;

    private final ConversionRepository conversionRepository;

    private final RestService restService;

    private final ConversionMapper conversionMapper;

    @Override
    public CurrencyResponseDto processConvertRequest(CurrencyRequestDto requestDto) {
        String fromCurrency = requestDto.fromCurrency().toString();
        String toCurrency = requestDto.toCurrency().toString();
        ExchangeRespDto responseBody = retrieveCurrencyResponseDto(fromCurrency, toCurrency);
        List<OpenConverterCurrencyRespDto> allCurrencyValues = responseBody.data()
                .values().stream()
                .toList();
        CurrencyResponseDto currencyResponseDto = getCurrencyResponseDto(requestDto, allCurrencyValues, fromCurrency);
        conversionRepository.save(new Conversion(
                finFlowUserService.getAuthenticatedUser().getUsername(),
                currencyResponseDto.fromCurrency(),
                currencyResponseDto.toCurrency(),
                currencyResponseDto.amount(),
                currencyResponseDto.resultSum(),
                currencyResponseDto.currencyRate()));
        return currencyResponseDto;
    }

    @Override
    public List<ConversionResponseDto> findAll() {
        return conversionRepository.findAll().stream()
                .map(conversionMapper::mapToResponseDto)
                .toList();
    }

    @Override
    public PageResult<ConversionResponseDto> findByAuthenticatedUser(Pageable pageable) {
        String loggedUser = finFlowUserService.getAuthenticatedUser().getUsername();
        Page<Conversion> currentPage = conversionRepository.findAllByLoggedUsername(loggedUser, pageable);
        List<ConversionResponseDto> items = currentPage.stream()
                .map(conversionMapper::mapToResponseDto)
                .toList();
        long count = conversionRepository.countByLoggedUsername(loggedUser);
        return new PageResult<>(items, count);
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

    private CurrencyResponseDto getCurrencyResponseDto(CurrencyRequestDto requestDto, List<OpenConverterCurrencyRespDto> allCurrencyValues, String fromCurrency) {
        for (OpenConverterCurrencyRespDto currencyRespDto : allCurrencyValues) {
            if (currencyRespDto.code().equals(requestDto.toCurrency().toString())) {
                double value = currencyRespDto.value();
                double sumToConvert = requestDto.amount();
                double convertedSum = sumToConvert * value;
                double roundedSum = new BigDecimal(convertedSum).setScale(3, RoundingMode.HALF_UP).doubleValue();
                double roundedCurrencyRate = new BigDecimal(value).setScale(3, RoundingMode.HALF_UP).doubleValue();
                return new CurrencyResponseDto(
                        CurrencyCode.valueOf(fromCurrency),
                        CurrencyCode.valueOf(currencyRespDto.code()),
                        sumToConvert, roundedSum, roundedCurrencyRate);
            }
        }
        throw new NoResponseFromExternalApiWasReceived("No response from external sources for the desired currency was received!");
    }
}
