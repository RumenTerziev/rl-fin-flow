package bg.lrsoft.rlfinflow.cucumber.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class FinanceManagementStepDefinition {



    @Given("20 BGN to convert")
    public void currency_to_convert() {
        assert true;
    }

    @And("the result currency is USD")
    public void theResultCurrencyIsUSD() {
    }

    @When("I make a request to convert them")
    public void iMakeARequestToConvertThem() {
    }

    @Then("the result is correct and status code is {int}")
    public void theResultIsCorrectAndStatusCodeIs(int arg0) {
    }
}
