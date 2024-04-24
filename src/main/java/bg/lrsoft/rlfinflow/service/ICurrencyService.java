package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.domain.dto.CurrencyRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyResponseDto;
import bg.lrsoft.rlfinflow.domain.model.Conversion;

import java.util.List;

public interface ICurrencyService {

    CurrencyResponseDto processConvertRequest(CurrencyRequestDto requestDto);

    List<Conversion> findAll();

    List<Conversion> findByAuthenticatedUser();
}
