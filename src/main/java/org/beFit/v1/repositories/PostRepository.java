package org.beFit.v1.repositories;

import org.beFit.v1.repositories.entities.PostEntity;

import java.util.List;

public interface PostRepository {
	PostEntity createPost(int userId, Integer imageId, String content);

	PostEntity getById(int postId);

	void archivePost(int postId);

	void changeVotes(int postId, int vote);

	List<PostEntity> getPostsByUser(int page, int pageSize, int userId);

	List<PostEntity> listFitGroupsPostsOfUser(int page, int pageSize, int userId);
}
