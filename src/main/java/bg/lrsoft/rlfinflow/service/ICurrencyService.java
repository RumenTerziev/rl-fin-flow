package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.domain.dto.CurrencyRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyResponseDto;

import java.util.List;

public interface ICurrencyService {

    List<CurrencyResponseDto> processConvertRequest(CurrencyRequestDto requestDto);
}
