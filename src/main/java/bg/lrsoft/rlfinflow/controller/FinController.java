package bg.lrsoft.rlfinflow.controller;

import bg.lrsoft.rlfinflow.domain.dto.CurrencyRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyResponseDto;
import bg.lrsoft.rlfinflow.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/finances")
public class FinController {

    private final CurrencyService currencyService;

    @GetMapping
    public String getMyFinances() {
        return "This page should be private!";
    }

    @GetMapping("/dummy-path")
    public List<CurrencyResponseDto> exchange(@RequestBody CurrencyRequestDto currencyRequestDto) {
        return currencyService.processConvertRequest(currencyRequestDto);
    }
}
