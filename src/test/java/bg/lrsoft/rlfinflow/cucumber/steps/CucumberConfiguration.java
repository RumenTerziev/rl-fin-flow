package bg.lrsoft.rlfinflow.cucumber.steps;

import bg.lrsoft.rlfinflow.testconfig.TestConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CucumberConfiguration {
}
