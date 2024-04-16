package bg.lrsoft.rlfinflow.cucumber.steps;

import bg.lrsoft.rlfinflow.testconfig.TestConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@CucumberContextConfiguration
@SpringBootTest(classes = TestConfig.class, webEnvironment = DEFINED_PORT)
public class CucumberConfiguration {
}
