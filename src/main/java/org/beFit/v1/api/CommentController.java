package org.beFit.v1.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.beFit.v1.api.models.CommentEditInput;
import org.beFit.v1.api.models.CommentInput;
import org.beFit.v1.api.models.PostInput;
import org.beFit.v1.core.AuthService;
import org.beFit.v1.core.CommentService;
import org.beFit.v1.core.UserService;
import org.beFit.v1.core.models.Comment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
@SecurityRequirement(name = "bearerAuth")
public class CommentController {
	private CommentService commentService;
	private AuthService authService;

	public CommentController(CommentService commentService, AuthService authService) {
		this.commentService = commentService;
		this.authService = authService;
	}

	@GetMapping(value = "/{id}")
	public Comment getComment(@PathVariable("id") int commentId) {
		return commentService.getComment(commentId);
	}

	@GetMapping(value = "/{id}/parent")
	public Optional<Comment> getParent(@PathVariable("id") int commentId) {
		return commentService.getParent(commentId);
	}

	@GetMapping(value = "/{id}/children")
	public Optional<List<Comment>> getChildren(@PathVariable("id") int commentId) {
		return commentService.getChildren(commentId);
	}

	@PostMapping(value = "/{id}/comment")
	public Comment createSubComment(@RequestBody CommentInput c, @PathVariable("id") int commentId, @RequestHeader("Authorization") String authToken) {
		return commentService.createSubComment(c.content, commentId, authToken);
	}

	//put the post_id in the local storage and when click new comment take the post_id
	@PostMapping
	public Comment createComment(@RequestBody CommentInput c, @RequestHeader("Authorization") String authToken) {
		return commentService.createComment(c.content, authService.getUserByAuthToken(authToken).get().id, c.post_id);
	}

	@PatchMapping(value = "/{id}/edit/")
	void editContent(@RequestBody CommentEditInput c, @PathVariable("id") int commentId) {
		commentService.editContent(commentId, c.content);
	}
}
