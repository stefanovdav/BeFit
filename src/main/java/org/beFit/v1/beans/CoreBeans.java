package org.beFit.v1.beans;

import com.cloudinary.Cloudinary;
import org.beFit.v1.auth.CloudinaryConfig;
import org.beFit.v1.core.*;
import org.beFit.v1.core.imageservice.CloudinaryService;
import org.beFit.v1.repositories.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CoreBeans {

	private final CloudinaryConfig cloudinaryConfig;

	public CoreBeans(CloudinaryConfig cloudinaryConfig) {
		this.cloudinaryConfig = cloudinaryConfig;
	}

	@Bean
	public UserService userService(UserRepository repository, FitGroupService fitGroupService) {
		return new UserService(repository, fitGroupService);
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
	public CloudinaryService cloudinaryService(ImageRepository imageRepository, Cloudinary c) {
		return new CloudinaryService(c, imageRepository);
	}

	@Bean
	public Cloudinary cloudinary() {
		return new Cloudinary(
				Map.of(
						"cloud_name", cloudinaryConfig.getCloudName(),
						"api_key", cloudinaryConfig.getApiKey(),
						"api_secret", cloudinaryConfig.getApiSecret()
				)
		);
	}
}
