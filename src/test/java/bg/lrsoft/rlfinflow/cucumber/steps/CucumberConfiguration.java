package bg.lrsoft.rlfinflow.cucumber.steps;

import bg.lrsoft.rlfinflow.testconfig.TestConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;


@AutoConfigureMockMvc
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
@CucumberContextConfiguration
public class CucumberConfiguration {
}
