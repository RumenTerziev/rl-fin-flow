package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.domain.dto.ConversionResponseDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyResponseDto;
import bg.lrsoft.rlfinflow.domain.model.PageResult;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICurrencyService {

    CurrencyResponseDto processConvertRequest(CurrencyRequestDto requestDto);

    List<ConversionResponseDto> findAll();

    PageResult<ConversionResponseDto> findByAuthenticatedUser(Pageable pageable);
}
