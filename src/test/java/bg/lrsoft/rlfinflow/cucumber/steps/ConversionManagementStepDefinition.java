package bg.lrsoft.rlfinflow.cucumber.steps;

import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;
import bg.lrsoft.rlfinflow.domain.dto.*;
import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.repository.FinFlowUserRepository;
import bg.lrsoft.rlfinflow.service.RestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static bg.lrsoft.rlfinflow.utils.TestFileUtils.readFileAsString;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

public class ConversionManagementStepDefinition {

    private CurrencyCode fromCurrency;

    private CurrencyCode toCurrency;

    private double amount;

    private ResponseEntity<CurrencyResponseDto> response;

    private final PasswordEncoder passwordEncoder = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    @Value("${first.fin-flow.user.username}")
    private String firstUserUsername;

    @Value("${first.fin-flow.user.password}")
    private String firstUserPassword;

    @Value("${first.fin-flow.user.email}")
    private String firstUserEmail;

    @Value("${first.fin-flow.user.authorities}")
    private List<String> firstUserAuthorities;

    @Value("${open.exchange.currency.convertor.url}")
    private String openExchangeUrl;

    @Autowired
    private RestService restService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FinFlowUserRepository finFlowUserRepository;

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
    public void I_make_a_request_to_convert_them() {

        initialize();
        ResponseEntity<Void> loginResponse = login();

        String toCurrency = this.toCurrency.toString();

        when(restService.getForEntity(anyString(), eq(ExchangeRespDto.class)))
                .thenReturn(new ResponseEntity<>(new ExchangeRespDto(new MetaInfDto(LocalDateTime.now()),
                        Map.of(toCurrency,
                                new OpenConverterCurrencyRespDto(toCurrency, 0.50))), OK));

        String url = "/converter/open-api-rates";

        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.setContentType(MediaType.APPLICATION_JSON);
        postHeaders.add(HttpHeaders.COOKIE, loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE));

        CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(fromCurrency, this.toCurrency, amount);

        HttpEntity<CurrencyRequestDto> postRequest = new HttpEntity<>(currencyRequestDto, postHeaders);

        response = testRestTemplate.exchange(url, HttpMethod.POST, postRequest, CurrencyResponseDto.class);
    }

    @Then("^the result is correct and status code is ([0-9]+)$")
    public void the_result_is_correct_and_status_code_is(int status) throws JsonProcessingException {
        String stringPath = "src/test/resources/testdata/convertedCurrency.json";
        String body = readFileAsString(stringPath);
        assertThat(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getBody())).isEqualTo(body);
        assertThat(status).isEqualTo(response.getStatusCode().value());
    }

    private void initialize() {
        FinFlowUser finFlowUser = new FinFlowUser(
                firstUserUsername,
                passwordEncoder.encode(firstUserPassword),
                firstUserEmail,
                AuthorityUtils.createAuthorityList(firstUserAuthorities));
        finFlowUserRepository.add(finFlowUser);
        System.out.printf("Added first user to db!!! %s%n", finFlowUser);
    }

    private ResponseEntity<Void> login() {
        MultiValueMap<String, String> loginParams = new LinkedMultiValueMap<>();
        loginParams.add("username", firstUserUsername);
        loginParams.add("password", firstUserPassword);

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> loginRequest = new HttpEntity<>(loginParams, loginHeaders);

        return testRestTemplate.postForEntity("/auth/login", loginRequest, Void.class);
    }
}
