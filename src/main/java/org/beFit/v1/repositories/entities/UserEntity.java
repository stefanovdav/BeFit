package org.beFit.v1.repositories.entities;

import org.beFit.v1.core.models.Role;

import java.math.BigDecimal;
import java.util.List;

public class UserEntity {
	public final Integer id;
	public final String username;
	public final String passwordHash;
	public final List<Role> roles;
	public final String avatarUrl;
	public final BigDecimal balance;

	public UserEntity(Integer id, String username, String passwordHash, List<Role> roles, String avatarUrl, BigDecimal balance) {
		this.id = id;
		this.username = username;
		this.passwordHash = passwordHash;
		this.roles = roles;
		this.avatarUrl = avatarUrl;
		this.balance = balance;
	}
}
