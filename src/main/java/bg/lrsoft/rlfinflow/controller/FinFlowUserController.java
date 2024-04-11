package bg.lrsoft.rlfinflow.controller;

import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserImportDto;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users", description = "User management APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class FinFlowUserController {

    private final FinFlowUserService finFlowUserService;

    @PostMapping("/register")
    public void register(@Valid @RequestBody FinFlowUserImportDto finFlowUserImportDto) {
        finFlowUserService.registerUser(finFlowUserImportDto);
    }
}
