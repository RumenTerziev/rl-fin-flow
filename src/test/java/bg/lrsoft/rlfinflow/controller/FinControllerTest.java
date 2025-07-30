package bg.lrsoft.rlfinflow.controller;

import bg.lrsoft.rlfinflow.domain.dto.*;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import bg.lrsoft.rlfinflow.service.CurrencyService;
import bg.lrsoft.rlfinflow.service.RestService;
import bg.lrsoft.rlfinflow.testconfig.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FinController.class)
@ContextConfiguration(classes = TestConfig.class)
public class FinControllerTest {

    @Value("${open.exchange.currency.convertor.url}")
    private String openExchangeUrl;

    @Autowired
    private RestService restService;

    @MockBean(name="OpenApiRateCurrencyService")
    private CurrencyService openApiRateCurrencyService;

    @MockBean(name="BNBRateCurrencyService")
    private CurrencyService bnbRateCurrencyService;

    @MockBean
    private FinFlowUserService finFlowUserService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser", password = "1234", roles = "USER")
    void testExchange_whenGivenRequestDto_shouldReturnValidResult() throws Exception {
        //Given
        String url = "/converter/open-api-rates";
        String toCurrency = "EUR";
        String fromCurrency = "BGN";
        double amount = 20.0;
        double expectedResultSum = 10.0;
        double mockedExchangeRate = 0.50;

        when(restService.getForEntity(
                openExchangeUrl.formatted(toCurrency, fromCurrency),
                ExchangeRespDto.class))
                .thenReturn(new ResponseEntity<>(new ExchangeRespDto(new MetaInfDto(LocalDateTime.now()),
                        Map.of(toCurrency, new OpenConverterCurrencyRespDto(toCurrency, mockedExchangeRate))), OK));

        CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(BGN, EUR, amount);

        CurrencyResponseDto responseDto = new CurrencyResponseDto(BGN, EUR, amount, expectedResultSum, mockedExchangeRate);
        when(openApiRateCurrencyService.processConvertRequest(currencyRequestDto)).thenReturn(responseDto);

        //When
        ResultActions result = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(currencyRequestDto)));

        //Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.fromCurrency").value(fromCurrency))
                .andExpect(jsonPath("$.toCurrency").value(toCurrency))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(jsonPath("$.resultSum").value(expectedResultSum))
                .andExpect(jsonPath("$.currencyRate").value(mockedExchangeRate));
    }
}
