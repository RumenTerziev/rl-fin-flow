package bg.lrsoft.rlfinflow.controller;

import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserRegisterDto;
import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserResponseDto;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "User management APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class FinFlowUserController {

    private final FinFlowUserService finFlowUserService;

    @PostMapping("/register")
    public void register(@Valid @RequestBody FinFlowUserRegisterDto finFlowUserImportDto) {
        finFlowUserService.registerUser(finFlowUserImportDto);
    }

    @GetMapping("/profile")
    public FinFlowUserResponseDto getMyProfile() {
        return finFlowUserService.getMyProfile();
    }
}
