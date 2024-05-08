package bg.lrsoft.rlfinflow.service.impl;

import bg.lrsoft.rlfinflow.config.mapper.ConversionMapper;
import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;
import bg.lrsoft.rlfinflow.domain.dto.*;
import bg.lrsoft.rlfinflow.domain.model.Conversion;
import bg.lrsoft.rlfinflow.domain.model.PageResult;
import bg.lrsoft.rlfinflow.repository.ConversionRepository;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import bg.lrsoft.rlfinflow.service.ICurrencyService;
import bg.lrsoft.rlfinflow.service.IRestService;
import bg.lrsoft.rlfinflow.service.exception.NoResponseFromExternalApiWasReceived;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyConvertService implements ICurrencyService {

    @Value("${open.exchange.currency.convertor.url}")
    private String currencyConvertorUrl;

    private final FinFlowUserService finFlowUserService;

    private final ConversionRepository conversionRepository;

    private final IRestService restService;

    private final ConversionMapper conversionMapper;

    @Override
    public CurrencyResponseDto processConvertRequest(CurrencyRequestDto requestDto) {
        String baseCurrency = requestDto.baseCurrency().toString();
        String currencyToConvertTo = requestDto.currencyToConvertTo().toString();
        ExchangeRespDto responseBody = retrieveCurrencyResponseDto(baseCurrency, currencyToConvertTo);
        List<OpenConverterCurrencyRespDto> allCurrencyValues = responseBody.data()
                .values().stream()
                .toList();
        CurrencyResponseDto currencyResponseDto = getCurrencyResponseDto(requestDto, allCurrencyValues, baseCurrency);
        conversionRepository.save(new Conversion(
                finFlowUserService.getAuthenticatedUser().getUsername(),
                currencyResponseDto.baseCurrency(),
                currencyResponseDto.currencyToConvertTo(),
                currencyResponseDto.sumToConvert(),
                currencyResponseDto.resultSum()));
        return currencyResponseDto;
    }

    @Override
    public List<ConversionResponseDto> findAll() {
        return conversionRepository.findAll().stream()
                .map(conversionMapper::mapToDto)
                .toList();
    }

    @Override
    public PageResult<ConversionResponseDto> findByAuthenticatedUser(Pageable pageable) {
        String loggedUser = finFlowUserService.getAuthenticatedUser().getUsername();
        Page<Conversion> currentPage = conversionRepository.findAllByLoggedUsername(loggedUser, pageable);
        List<ConversionResponseDto> items = currentPage.stream()
                .map(conversionMapper::mapToDto)
                .toList();
        long count = conversionRepository.countByLoggedUsername(loggedUser);
        return new PageResult<>(items, count);
    }

    private ExchangeRespDto retrieveCurrencyResponseDto(String baseCurrency, String currencyToConvertTo) {
        ResponseEntity<ExchangeRespDto> response = restService.getForEntity(
                currencyConvertorUrl.formatted(currencyToConvertTo, baseCurrency),
                ExchangeRespDto.class);
        ExchangeRespDto responseBody = response.getBody();
        if (responseBody == null) {
            throw new NoResponseFromExternalApiWasReceived("No response from external sources was received!");
        }
        return responseBody;
    }

    private CurrencyResponseDto getCurrencyResponseDto(CurrencyRequestDto requestDto, List<OpenConverterCurrencyRespDto> allCurrencyValues, String baseCurrency) {
        for (OpenConverterCurrencyRespDto currencyRespDto : allCurrencyValues) {
            if (currencyRespDto.code().equals(requestDto.currencyToConvertTo().toString())) {
                double value = currencyRespDto.value();
                double sumToConvert = requestDto.sumToConvert();
                double convertedSum = sumToConvert * value;
                return new CurrencyResponseDto(
                        CurrencyCode.valueOf(baseCurrency),
                        CurrencyCode.valueOf(currencyRespDto.code()),
                        sumToConvert, convertedSum);
            }
        }
        throw new NoResponseFromExternalApiWasReceived("No response from external sources for the desired currency was received!");
    }
}
