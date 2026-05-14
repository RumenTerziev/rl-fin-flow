package bg.lrsoft.rlfinflow.cucumber.steps;

import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyResponseDto;
import bg.lrsoft.rlfinflow.domain.dto.ExchangeRespDto;
import bg.lrsoft.rlfinflow.domain.dto.MetaInfDto;
import bg.lrsoft.rlfinflow.domain.dto.OpenConverterCurrencyRespDto;
import bg.lrsoft.rlfinflow.service.RestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static bg.lrsoft.rlfinflow.utils.TestFileUtils.readFileAsString;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

/**
 * End-to-end exercise of the converter for the anonymous flow: per the new GDPR design,
 * the converter endpoint is callable without authentication and nothing is persisted.
 */
public class ConversionManagementStepDefinition {

    private CurrencyCode fromCurrency;
    private CurrencyCode toCurrency;
    private double amount;
    private ResponseEntity<CurrencyResponseDto> response;

    @Autowired
    private RestService restService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Given("^([0-9]+(?:\\.[0-9]+)?)\\s+([A-Z]+)\\s+to\\s+convert$")
    public void convert(double sumToConvert, String fromCurrency) {
        this.fromCurrency = CurrencyCode.valueOf(fromCurrency);
        this.amount = sumToConvert;
    }

    @And("^the result currency is ([A-Z]+)$")
    public void the_result_currency_is(String toCurrency) {
        this.toCurrency = CurrencyCode.valueOf(toCurrency);
    }

    @When("^I make a request to convert them$")
    public void i_make_a_request_to_convert_them() {
        String toCurrencyCode = this.toCurrency.toString();

        when(restService.getForEntity(anyString(), eq(ExchangeRespDto.class)))
                .thenReturn(new ResponseEntity<>(new ExchangeRespDto(
                        new MetaInfDto(LocalDateTime.now()),
                        Map.of(toCurrencyCode, new OpenConverterCurrencyRespDto(toCurrencyCode, 0.50))), OK));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        CurrencyRequestDto body = new CurrencyRequestDto(fromCurrency, toCurrency, amount);
        HttpEntity<CurrencyRequestDto> request = new HttpEntity<>(body, headers);

        response = testRestTemplate.exchange(
                "/converter/open-api-rates", HttpMethod.POST, request, CurrencyResponseDto.class);
    }

    @Then("^the result is correct and status code is ([0-9]+)$")
    public void the_result_is_correct_and_status_code_is(int status) throws JsonProcessingException {
        String expected = readFileAsString("src/test/resources/testdata/convertedCurrency.json");
        assertThat(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getBody()))
                .isEqualTo(expected);
        assertThat(status).isEqualTo(response.getStatusCode().value());
    }
}
