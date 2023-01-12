package org.beFit.v1.repositories;

import org.beFit.v1.core.models.Role;
import org.beFit.v1.repositories.entities.UserEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
	UserEntity createUser(String username, String password, List<Role> roles);

	UserEntity getUser(int userId);

	void changeBalance(int userId, BigDecimal money);

	void changeAvatar(int userId, int image_id);

	void changePassword(int userId, String newPassword);

	List<String> getUserMemories(int userId);

	void addUserToGroup(int userId, int groupId);

	String createAuthToken(Integer userId, String authToken);

	Optional<UserEntity> getUser(String username);

	Optional<UserEntity> getUserByAuthToken(String authToken);

	void deleteUser(int id);
}
