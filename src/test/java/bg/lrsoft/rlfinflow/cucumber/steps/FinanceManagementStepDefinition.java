package bg.lrsoft.rlfinflow.cucumber.steps;

import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;
import bg.lrsoft.rlfinflow.domain.dto.*;
import bg.lrsoft.rlfinflow.service.IRestService;
import bg.lrsoft.rlfinflow.service.impl.CurrencyConvertService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;

public class FinanceManagementStepDefinition {

    private CurrencyCode baseCurrency;

    private CurrencyCode currencyToConvertTo;

    private double sumToConvert;

    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    private ResponseEntity<CurrencyResponseDto> response;

    @Value("${first.fin-flow.user.username}")
    private String firstUserUsername;

    @Value("${first.fin-flow.user.password}")
    private String firstUserPassword;

    @Value("${open.exchange.currency.convertor.url}")
    private String openExchangeUrl;

    @Autowired
    private IRestService restService;

    @Autowired
    private CurrencyConvertService currencyService;

    @Given("^([0-9]+(?:\\.[0-9]+)?)\\s+([A-Z]+)\\s+to\\s+convert$")
    public void convert(double sumToConvert, String baseCurrency) {
        this.baseCurrency = CurrencyCode.valueOf(baseCurrency);
        this.sumToConvert = sumToConvert;
    }

    @And("^the result currency is ([A-Z]+)$")
    public void the_result_currency_is(String currencyToConvertTo) {
        this.currencyToConvertTo = CurrencyCode.valueOf(currencyToConvertTo);
    }

    @When("^I make a request to convert them$")
    public void I_make_a_request_to_convert_them() {
        String currencyToConvertTo = this.currencyToConvertTo.toString();
        when(restService.getForEntity(
                openExchangeUrl.formatted(currencyToConvertTo, baseCurrency.toString()),
                Object.class))
                .thenReturn(new ResponseEntity<>(new ExchangeRespDto(new MetaInfDto(LocalDateTime.now()),
                        Map.of(currencyToConvertTo, new OpenConverterCurrencyRespDto(currencyToConvertTo, 0.51))), OK));

        CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(baseCurrency, this.currencyToConvertTo, sumToConvert);
        URI uri = URI.create(testRestTemplate.getRootUri() + "/finances/converter");
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(firstUserUsername, firstUserPassword);
        RequestEntity<CurrencyRequestDto> currencyRequest = new RequestEntity<>(currencyRequestDto, headers, POST, uri);
        response = testRestTemplate.exchange(currencyRequest, CurrencyResponseDto.class);
    }

    @Then("^the result is correct and status code is ([0-9]+)$")
    public void the_result_is_correct_and_status_code_is(int status) {
        assertEquals(status, response.getStatusCode().value());
    }
}
