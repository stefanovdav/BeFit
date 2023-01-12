package org.beFit.v1.api;

import org.beFit.v1.core.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private UserService userService;

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
