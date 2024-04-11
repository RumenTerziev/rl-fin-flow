package bg.lrsoft.rlfinflow.init;

import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final FinFlowUserService basicUserDetailsService;

    @Override
    public void run(String... args) throws Exception {
        basicUserDetailsService.initialize();
    }
}
