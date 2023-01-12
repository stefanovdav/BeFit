package org.beFit.v1.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public class User {
	public final Integer id;
	public final String username;
	public final List<Role> roles;
	public final Integer imageId;
	public final BigDecimal balance;


	public User(Integer id,
				String username,
				List<Role> roles,
				Integer imageId,
				BigDecimal balance) {
		this.id = id;
		this.username = username;
		this.roles = roles;
		this.imageId = imageId;
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", username='" + username + '\'' +
				", roles=" + roles +
				", imageId=" + imageId +
				", balance=" + balance +
				'}';
	}
}
