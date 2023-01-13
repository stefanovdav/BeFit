package org.beFit.v1.beans;

import com.cloudinary.Cloudinary;
import org.beFit.v1.core.*;
import org.beFit.v1.core.imageservice.CloudinaryService;
import org.beFit.v1.repositories.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreBeans {
	@Bean
	public UserService userService(UserRepository repository) {
		return new UserService(repository);
	}

	@Bean
	public PostService postService(PostRepository postRepository,
								   UserRepository userRepository,
								   AuthService authService) {
		return new PostService(postRepository, userRepository, authService);
	}

	@Bean
	public CommentService commentService(CommentRepository commentRepository,
										 UserRepository userRepository,
										 AuthService authService) {

		return new CommentService(commentRepository, userRepository, authService);
	}

	@Bean
	public FitGroupService fitGroupService(FitGroupRepository fitGroupRepository) {
		return new FitGroupService(fitGroupRepository);
	}

	@Bean
	public AuthService authService(UserRepository userRepository) {
		return new AuthService(userRepository);
	}

	@Bean
	CloudinaryService cloudinaryService(ImageRepository imageRepository, Cloudinary c) {
		return new CloudinaryService(c, imageRepository);
	}
}
