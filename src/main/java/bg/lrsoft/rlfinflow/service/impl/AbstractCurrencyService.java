package bg.lrsoft.rlfinflow.service.impl;

import bg.lrsoft.rlfinflow.config.mapper.ConversionMapper;
import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;
import bg.lrsoft.rlfinflow.domain.dto.ConversionResponseDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyResponseDto;
import bg.lrsoft.rlfinflow.domain.model.Conversion;
import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.domain.model.PageResult;
import bg.lrsoft.rlfinflow.repository.ConversionRepository;
import bg.lrsoft.rlfinflow.security.FinFlowOath2User;
import bg.lrsoft.rlfinflow.service.CurrencyService;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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
        String loggedUser = getLoggedUserUserName();
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
        return new CurrencyResponseDto(
                CurrencyCode.valueOf(fromCurrency),
                CurrencyCode.valueOf(toCurrency),
                amount, roundedSum, roundedCurrencyRate);
    }

    protected Conversion getConversionFromRespDto(CurrencyResponseDto responseDto) {
        String loggedUserUserName = getLoggedUserUserName();
        return new Conversion(
                loggedUserUserName,
                responseDto.fromCurrency(),
                responseDto.toCurrency(),
                responseDto.amount(),
                responseDto.resultSum(),
                responseDto.currencyRate());
    }

    private static String getLoggedUserUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedUser;
        if (principal instanceof FinFlowUser finFlowUser) {
            loggedUser = finFlowUser.getUsername();
        } else if (principal instanceof FinFlowOath2User finFlowOath2User) {
            loggedUser = finFlowOath2User.getName();
        } else {
            throw new SecurityException("Invalid user");
        }
        return loggedUser;
    }
}