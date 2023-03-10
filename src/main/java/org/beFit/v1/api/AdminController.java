package org.beFit.v1.api;

import org.beFit.v1.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

	private final UserService userService;

	@Autowired
	public AdminController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(value = "/whoisking")
	public String getTheKing() {
		return "you are";
	}

	@GetMapping(value = "/deleteUser")
	public void deleteUser(@RequestParam Integer userId) {
		userService.deleteUser(userId);
	}
}
