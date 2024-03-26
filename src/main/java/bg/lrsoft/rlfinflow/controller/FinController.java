package bg.lrsoft.rlfinflow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FinController {

    @GetMapping("/finances")
    public String getMyFinances() {
        return "This page should be private!";
    }
}
