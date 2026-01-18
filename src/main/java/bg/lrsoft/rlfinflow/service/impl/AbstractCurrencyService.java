package bg.lrsoft.rlfinflow.service.impl;

import bg.lrsoft.rlfinflow.config.mapper.ConversionMapper;
import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;
import bg.lrsoft.rlfinflow.domain.dto.ConversionResponseDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyResponseDto;
import bg.lrsoft.rlfinflow.domain.model.Conversion;
import bg.lrsoft.rlfinflow.domain.model.PageResult;
import bg.lrsoft.rlfinflow.repository.ConversionRepository;
import bg.lrsoft.rlfinflow.service.CurrencyService;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCurrencyService implements CurrencyService {

    protected final FinFlowUserService finFlowUserService;

    protected final ConversionRepository conversionRepository;

    protected final ConversionMapper conversionMapper;

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

    protected CurrencyResponseDto getCurrencyResponseDto(String fromCurrency, String toCurrency, double amount, double rate) {
        double convertedSum = amount * rate;
        double roundedSum = new BigDecimal(convertedSum).setScale(4, RoundingMode.HALF_UP).doubleValue();
        double roundedCurrencyRate = new BigDecimal(rate).setScale(4, RoundingMode.HALF_UP).doubleValue();
        log.info("TO CURRENCY: {}", toCurrency);
        return new CurrencyResponseDto(
                CurrencyCode.valueOf(fromCurrency),
                CurrencyCode.valueOf(toCurrency),
                amount, roundedSum, roundedCurrencyRate);
    }

    protected Conversion getConversionFromRespDto(CurrencyResponseDto responseDto) {
        return new Conversion(
                finFlowUserService.getAuthenticatedUser().getUsername(),
                responseDto.fromCurrency(),
                responseDto.toCurrency(),
                responseDto.amount(),
                responseDto.resultSum(),
                responseDto.currencyRate());
    }
}