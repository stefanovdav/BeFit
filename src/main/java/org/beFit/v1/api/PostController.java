package org.beFit.v1.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.beFit.v1.api.models.PostInput;
import org.beFit.v1.core.AuthService;
import org.beFit.v1.core.PostService;
import org.beFit.v1.core.UserService;
import org.beFit.v1.core.models.Post;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/posts")
@SecurityRequirement(name = "bearerAuth")
public class PostController {
	private PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping(value = "/{id}")
	public Post getPost(@PathVariable("id") Integer id) {
		return postService.getById(id);
	}

	@PatchMapping(value = "/{id}/archive")
	public void archivePost(@PathVariable("id") Integer id) {
		postService.archivePost(id);
	}

	@PatchMapping(value = "/{id}/upvote")
	public void upvote(@PathVariable("id") Integer postId) {
		postService.changeVotes(postId, 1);
	}

	@PatchMapping(value = "/{id}/downvote")
	public void downvote(@PathVariable("id") Integer postId) {
		postService.changeVotes(postId, -1);
	}

	@PostMapping
	public Post createPost(@RequestHeader("Authorization") String authToken, @RequestBody PostInput p) {
		return postService.createPost(authToken, p.imageId, p.content);
	}

	//TODO:Make a hard delete
//	@DeleteMapping(value = "/{id}")
//	@PreAuthorize("@securityValidation.isPostOwner(#authToken, #id) == true")
//	public void deletePost(
//			@RequestHeader("Authorization") String authToken,
//			@PathVariable("id") Integer id) {
//		postService.delete(id);
//	}

	//TODO:Add pagination and only the photo
	@GetMapping(value = "/memories/{page}/{pageSize}")
	public List<Post> getUserPastPosts(@RequestHeader("Authorization") String authToken,
									   @PathVariable("page") int page,
									   @PathVariable("pageSize") int pageSize) {
		return postService.getPostsByUser(page, pageSize, authToken);
	}

	@GetMapping(value = "/feed/{page}/{pageSize}")
	public List<Post> listFitGroupsPostsOfUser(@PathVariable("page") int page,
											   @PathVariable("pageSize") int pageSize,
											   @RequestHeader("Authorization") String authToken) {
		return postService.listFitGroupsPostsOfUser(page, pageSize, authToken);
	}
}
