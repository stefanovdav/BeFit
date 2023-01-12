package org.beFit.v1.auth;

import org.beFit.v1.core.AuthService;
import org.beFit.v1.core.models.Role;
import org.beFit.v1.repositories.PostRepository;
import org.beFit.v1.repositories.UserRepository;
import org.beFit.v1.repositories.entities.PostEntity;
import org.beFit.v1.repositories.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SecurityValidation {

	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final AuthService authService;

	public SecurityValidation(UserRepository userRepository, PostRepository postRepository, AuthService authService) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
		this.authService = authService;
	}


	public boolean isPostOwner(String authHeader, Integer postId) {
		String[] parts = authHeader.split(" ");
		Optional<UserEntity> userEntity = userRepository.getUserByAuthToken(parts[1]);
		PostEntity postEntity = postRepository.getById(postId);
		return postEntity.userId == userEntity.get().id;
	}

	public boolean isAccountOwner(String authHeader, Integer userId) {
		int user_id_byAuth = userRepository.getUserByAuthToken(authHeader).get().id;
		return userId == user_id_byAuth;
	}

	public boolean isAccountAdmin(String authHeader) {
		List<Role> roles = userRepository.getUserByAuthToken(authHeader).get().roles;
		return roles.contains(Role.ADMIN);
	}
}
