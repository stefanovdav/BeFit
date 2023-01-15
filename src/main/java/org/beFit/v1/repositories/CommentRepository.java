package org.beFit.v1.repositories;

import org.beFit.v1.repositories.entities.CommentEntity;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
	CommentEntity getComment(int commentId);

	Optional<CommentEntity> getParent(int commentId);

	Optional<List<CommentEntity>> getChildren(int commentId);

	CommentEntity createFirstComment(String content, int userId, int post_id);

	CommentEntity createSubComment(String content, int parentId, int userId);

	void editContent(int commentId, String newContent);

	Optional<List<CommentEntity>> showPostComments(int postId);
}
