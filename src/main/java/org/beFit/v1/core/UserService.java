package org.beFit.v1.core;

import org.beFit.v1.core.models.User;
import org.beFit.v1.repositories.UserRepository;
import org.beFit.v1.repositories.entities.UserEntity;

import java.math.BigDecimal;
import java.util.List;

public class UserService {
	private final UserRepository repository;
	private final FitGroupService fitGroupService;

	public UserService(UserRepository repository, FitGroupService fitGroupService) {
		this.repository = repository;
		this.fitGroupService = fitGroupService;
	}

	public User getUser(int userId) {
		UserEntity u = repository.getUser(userId);
		return Mappers.fromUserEntity(u);
	}

	public void changeBalance(Integer id, BigDecimal money) {
		repository.changeBalance(id, money);
	}

	public void changeAvatar(Integer id, int image_id) {
		repository.changeAvatar(id, image_id);
	}

	public void changePassword(Integer id, String newPassword) {
		repository.changePassword(id, newPassword);
	}

	public List<String> getUserMemories(Integer id) {
		return repository.getUserMemories(id);
	}

	public void addUserToGroup(Integer id, int groupId) {
		//TODO: signal when there arent enough assets to enter
		BigDecimal stake = fitGroupService.getFitGroup(groupId).stake;
		if (stake.equals(BigDecimal.ZERO)) {
			repository.addUserToGroup(id, groupId, stake);
		} else {
			BigDecimal balance = getUser(id).balance;
			if (balance.compareTo(stake) >= 0) {
				changeBalance(id, stake.negate());
				repository.addUserToGroup(id, groupId, stake);
			}
		}
	}

	public void deleteUser(Integer id) {
		repository.deleteUser(id);
	}

	public BigDecimal showUserFrozenAssetsInGroup(int id, Integer groupId) {
		return repository.showUserFrozenAssetsInGroup(id, groupId);
	}
}