package org.beFit.v1.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public class User {
	public final Integer id;
	public final String username;
	public final List<Role> roles;
	public final String avatarUrl;
	public final BigDecimal balance;

	@JsonCreator
	public User(@JsonProperty("id") Integer id,
				@JsonProperty("username") String username,
				List<Role> roles,
				@JsonProperty("avatarUrl") String avatarUrl,
				@JsonProperty("balance") BigDecimal balance) {
		this.id = id;
		this.username = username;
		this.roles = roles;
		this.avatarUrl = avatarUrl;
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", username='" + username + '\'' +
				", roles=" + roles +
				", avatarUrl='" + avatarUrl + '\'' +
				", balance=" + balance +
				'}';
	}
}
