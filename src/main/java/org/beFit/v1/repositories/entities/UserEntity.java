package org.beFit.v1.repositories.entities;

import org.beFit.v1.core.models.Role;

import java.math.BigDecimal;
import java.util.List;

//TODO user record
public class UserEntity {
	public final Integer id;
	public final String username;
	public final String passwordHash;
	public final List<Role> roles;
	public final Integer imageId;
	public final BigDecimal balance;

	public UserEntity(Integer id, String username, String passwordHash, List<Role> roles, Integer imageId, BigDecimal balance) {
		this.id = id;
		this.username = username;
		this.passwordHash = passwordHash;
		this.roles = roles;
		this.imageId = imageId;
		this.balance = balance;
	}
}
