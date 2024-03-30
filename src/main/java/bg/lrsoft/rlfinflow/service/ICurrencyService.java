package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.domain.dto.CurrencyRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICurrencyService {

    List<CurrencyResponseDto> processConvertRequest(CurrencyRequestDto requestDto);
}
