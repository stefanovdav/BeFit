package org.beFit.v1.core;

import org.beFit.v1.core.models.User;
import org.beFit.v1.repositories.UserRepository;
import org.beFit.v1.repositories.entities.UserEntity;

import java.math.BigDecimal;
import java.util.List;

public class UserService {
	private final UserRepository repository;

	public UserService(UserRepository repository) {
		this.repository = repository;
	}

	public User getUser(int userId) {
		UserEntity u = repository.getUser(userId);
		return Mappers.fromUserEntity(u);
	}

	public void changeBalance(Integer id, BigDecimal money) {
		repository.changeBalance(id, money);
	}

	public void changeAvatar(Integer id, String newUrl) {
		repository.changeAvatar(id, newUrl);
	}

	public void changePassword(Integer id, String newPassword) {
		repository.changePassword(id, newPassword);
	}

	public List<String> getUserMemories(Integer id) {
		return repository.getUserMemories(id);
	}

	public void addUserToGroup(Integer id, int groupId) {
		repository.addUserToGroup(id, groupId);
	}

	public void deleteUser(Integer id) {
		repository.deleteUser(id);
	}
}