package org.beFit.v1.core;

import org.beFit.v1.core.models.Comment;
import org.beFit.v1.repositories.CommentRepository;
import org.beFit.v1.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommentService {
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final AuthService authService;

	public CommentService(CommentRepository commentRepository, UserRepository userRepository, AuthService authService) {
		this.commentRepository = commentRepository;
		this.userRepository = userRepository;
		this.authService = authService;
	}

	public Comment getComment(int commentId) {
		return Mappers.fromCommentEntity(
				commentRepository.getComment(commentId)
		);
	}

	public Optional<Comment> getParent(int commentId) {
		return Optional.of(Mappers.fromCommentEntity(
				commentRepository.getParent(commentId).get()
		));
	}

	public Optional<List<Comment>> getChildren(int commentId) {
		return commentRepository.getChildren(commentId)
				.map(commentEntities -> commentEntities.stream()
						.map(Mappers::fromCommentEntity)
						.collect(Collectors.toList())
				);
	}

	public Comment createComment(String content, Integer id, Integer post_id) {
		return Mappers.fromCommentEntity(
				commentRepository.createFirstComment(content, id, post_id)
		);
	}

	public Comment createSubComment(String content, int parentId, String authToken) {

		return Mappers.fromCommentEntity(
				commentRepository.createSubComment(content, parentId,
						authService.getUserByAuthToken(authToken).id)
		);
	}

	public void editContent(int commentId, String newContent) {
		commentRepository.editContent(commentId, newContent);
	}
}
//every time i create a post in the front end, i assign it an id that is the same as the post_id in the DB or
// you can make query to take the id by parent and datetime stamp