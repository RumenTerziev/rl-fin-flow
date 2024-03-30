package bg.lrsoft.rlfinflow.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Home", description = "Home page APIs")
@RestController
public class HomeController {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success"
            )
    })
    @GetMapping("/home")
    public String home() {
        return "Hi!";
    }
}
