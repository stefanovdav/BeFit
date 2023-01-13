package org.beFit.v1.beans;

import org.beFit.v1.repositories.*;
import org.beFit.v1.repositories.mariadb.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class RepositoryBeans {

	@Bean
	public UserRepository userRepository (
			TransactionTemplate txTemplate, JdbcTemplate jdbcTemplate) {
		return new MariaDbUserRepository(txTemplate, jdbcTemplate);
	}

	@Bean
	public PostRepository postRepository (
			TransactionTemplate txTemplate, JdbcTemplate jdbcTemplate) {
		return new MariaDbPostRepository(txTemplate, jdbcTemplate);
	}

	@Bean
	public CommentRepository commentRepository (
			TransactionTemplate txTemplate, JdbcTemplate jdbcTemplate) {
		return new MariaDbCommentRepository(jdbcTemplate, txTemplate);
	}

	@Bean
	public FitGroupRepository fitGroupRepository (
			TransactionTemplate txTemplate, JdbcTemplate jdbcTemplate) {
		return new MariaDbFitGroupRepository(txTemplate, jdbcTemplate);
	}

	@Bean
	public ImageRepository imageRepository (TransactionTemplate txTemplate, JdbcTemplate jdbcTemplate) {
		return new MariaDbImageRepository(jdbcTemplate, txTemplate);
	}
}
