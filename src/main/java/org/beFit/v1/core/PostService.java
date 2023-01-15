package org.beFit.v1.core;

import org.beFit.v1.core.models.Post;
import org.beFit.v1.repositories.PostRepository;
import org.beFit.v1.repositories.UserRepository;
import org.springframework.data.relational.core.sql.In;

import java.util.List;
import java.util.stream.Collectors;

public class PostService {
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final AuthService authService;

	public PostService(PostRepository postRepository, UserRepository userRepository, AuthService authService) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.authService = authService;
	}

	public Post createPost(String authToken, Integer imageId, String content) {
		int userId = authService.getUserByAuthToken(authToken).get().id;
		return Mappers.fromPostEntity(
				postRepository.createPost(userId, imageId, content)
		);
	}

	public Post getById(int postId) {
		return Mappers.fromPostEntity(
				postRepository.getById(postId)
		);
	}

	public void archivePost(int postId) {
		postRepository.archivePost(postId);
	}

	public void changeVotes(int postId, int vote) {
		postRepository.changeVotes(postId, vote);
	}

	public List<Post> getPostsByUser(int page, int pageSize, String authToken) {
		int userId = authService.getUserByAuthToken(authToken).get().id;
		return postRepository.getPostsByUser(page, pageSize, userId)
				.stream()
				.map(Mappers::fromPostEntity)
				.collect(Collectors.toList());
	}

	public List<Post> listFitGroupsPostsOfUser(int page, int pageSize, String authToken) {
		int id = userRepository.getUserByAuthToken(authToken).get().id;
		return postRepository.listFitGroupsPostsOfUser(page, pageSize, id)
				.stream()
				.map(Mappers::fromPostEntity)
				.collect(Collectors.toList());
	}
}
