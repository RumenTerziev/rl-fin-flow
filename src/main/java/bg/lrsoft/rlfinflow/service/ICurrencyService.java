package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.domain.dto.CurrencyRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyResponseDto;

public interface ICurrencyService {

    CurrencyResponseDto processConvertRequest(CurrencyRequestDto requestDto);
}
