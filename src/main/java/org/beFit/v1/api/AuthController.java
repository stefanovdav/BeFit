package org.beFit.v1.api;

import org.beFit.v1.api.models.LoginInput;
import org.beFit.v1.core.AuthService;
import org.beFit.v1.dto.AuthDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login")
    public AuthDTO login(@RequestBody LoginInput input) {
        String authToken = authService.login(input.username, input.password);

        return new AuthDTO(authToken);
    }

    @PostMapping(value = "/register")
    public AuthDTO register(@RequestBody LoginInput input) {
        authService.register(input.username, input.password);
        String authToken = authService.login(input.username, input.password);
        return new AuthDTO(authToken);
    }
}
