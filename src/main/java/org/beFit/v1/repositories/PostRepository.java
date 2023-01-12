package org.beFit.v1.repositories;

import org.beFit.v1.repositories.entities.CommentEntity;
import org.beFit.v1.repositories.entities.PostEntity;
import org.beFit.v1.repositories.entities.UserEntity;

import java.util.List;

public interface PostRepository {
	PostEntity createPost(int userId, Integer imageId, String content);

	PostEntity getById(int postId);

	void archivePost(int postId);

	void changeVotes(int postId, int vote);

	List<PostEntity> getPostsByUser(int userId);

	List<PostEntity> listFitGroupsPostsOfUser(int page, int pageSize, int userId);
}
