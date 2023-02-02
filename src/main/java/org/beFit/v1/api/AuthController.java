package org.beFit.v1.api;

import org.beFit.v1.api.models.LoginInput;
import org.beFit.v1.core.AuthService;
import org.beFit.v1.core.imageservice.CloudinaryService;
import org.beFit.v1.dto.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
	private final AuthService authService;

	private final CloudinaryService cloudinaryService;

	@Autowired
	public AuthController(AuthService authService, CloudinaryService cloudinaryService) {
		this.authService = authService;
		this.cloudinaryService = cloudinaryService;
	}

	@PostMapping(value = "/login")
	public LoginDTO login(@RequestBody LoginInput input) {
		String authToken = authService.login(input.username, input.password);
		String url = cloudinaryService.getImage(authService.getUserByAuthToken(authToken).imageId).getUrl();
		return new LoginDTO(authToken, input.username, url);
	}

	@PostMapping(value = "/register")
	public void register(@RequestBody LoginInput input) {
		authService.register(input.username, input.password);
	}
}
