package bg.lrsoft.rlfinflow.controller;

import bg.lrsoft.rlfinflow.domain.dto.*;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import bg.lrsoft.rlfinflow.service.ICurrencyService;
import bg.lrsoft.rlfinflow.service.IRestService;
import bg.lrsoft.rlfinflow.testconfig.TestConfig;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Map;

import static bg.lrsoft.rlfinflow.domain.constant.CurrencyCode.BGN;
import static bg.lrsoft.rlfinflow.domain.constant.CurrencyCode.EUR;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FinController.class)
@ContextConfiguration(classes = TestConfig.class)
public class FinControllerTest {

    @Value("${open.exchange.currency.convertor.url}")
    private String openExchangeUrl;

    @Autowired
    private IRestService restService;

    @MockBean
    private ICurrencyService currencyService;

    @MockBean
    private FinFlowUserService finFlowUserService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Test
    @WithMockUser(username = "testuser", password = "1234", roles = "USER")
    void testExchange_whenGivenRequestDto_shouldReturnValidResult() throws Exception {
        //Given
        String url = "/finances/converter";
        String currencyToConvertTo = "EUR";
        String baseCurrency = "BGN";

        when(restService.getForEntity(
                openExchangeUrl.formatted(currencyToConvertTo, baseCurrency),
                ExchangeRespDto.class))
                .thenReturn(new ResponseEntity<>(new ExchangeRespDto(new MetaInfDto(LocalDateTime.now()),
                        Map.of(currencyToConvertTo, new OpenConverterCurrencyRespDto(currencyToConvertTo, 0.50))), OK));

        CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(BGN, EUR, 20.0);

        CurrencyResponseDto responseDto = new CurrencyResponseDto(BGN, EUR, 20, 10);
        when(currencyService.processConvertRequest(currencyRequestDto)).thenReturn(responseDto);

        //When
        ResultActions result = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(gson.toJson(currencyRequestDto)));

        //Then
        result.andExpect(status().isOk());
    }
}
