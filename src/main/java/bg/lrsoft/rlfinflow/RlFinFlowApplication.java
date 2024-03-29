package bg.lrsoft.rlfinflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class RlFinFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(RlFinFlowApplication.class, args);
    }
}
