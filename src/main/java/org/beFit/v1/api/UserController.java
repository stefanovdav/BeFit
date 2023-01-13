package org.beFit.v1.api;

import org.beFit.v1.core.CommentService;
import org.beFit.v1.core.PostService;
import org.beFit.v1.core.UserService;
import org.beFit.v1.core.models.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

	private UserService userService;
	private PostService postService;
	private CommentService commentService;

	public UserController(UserService userService, PostService postService, CommentService commentService) {
		this.userService = userService;
		this.postService = postService;
		this.commentService = commentService;
	}

	@GetMapping(value = "/{id}")
	public User getUser(@PathVariable("id") Integer id) {
		return userService.getUser(id);
	}

	@PatchMapping(value = "/{id}/balance")
	public void changeBalance(@PathVariable("id") Integer id, @RequestParam("money") BigDecimal money) {
		userService.changeBalance(id, money);
	}

	@PatchMapping(value = "/{id}/avatar")
	public void changeAvatar(@PathVariable("id") Integer id, @RequestParam("image_id") int image_id) {
		userService.changeAvatar(id, image_id);
	}

	//TODO:How to change password and delete comments
//	public void changePassword(int userId, String newPassword) {
//
//	}

	//TODO:Add pagination
	@GetMapping(value = "/{id}/memories")
	public List<String> getUserMemories(@PathVariable("id") Integer id) {
		return userService.getUserMemories(id);
	}

	@GetMapping(value = "/{id}/group/{groupId}")
	public void addUserToGroup(@PathVariable("id") Integer id, @RequestParam("groupId") int groupId) {
		userService.addUserToGroup(id, groupId);
	}

	@DeleteMapping(value = "/{id}/delete")
	@PreAuthorize("@securityValidation.isAccountOwner(#authToken, #id) == true")
	public void deleteYourAccount(@PathVariable("id") Integer id, @RequestHeader("Authorization") String authToken) {
		userService.deleteUser(id);
	}


}
