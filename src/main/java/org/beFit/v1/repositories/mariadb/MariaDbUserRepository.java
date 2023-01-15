package org.beFit.v1.repositories.mariadb;

import org.beFit.v1.core.models.Role;
import org.beFit.v1.repositories.UserRepository;
import org.beFit.v1.repositories.entities.FitGroupEntity;
import org.beFit.v1.repositories.entities.UserEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

import static org.beFit.v1.repositories.mariadb.MariaDbUserRepository.Queries.*;

public class MariaDbUserRepository implements UserRepository {

	private final TransactionTemplate txTemplate;
	private final JdbcTemplate jdbc;

	public MariaDbUserRepository(TransactionTemplate txTemplate, JdbcTemplate jdbc) {
		this.txTemplate = txTemplate;
		this.jdbc = jdbc;
	}

	@Override
	public UserEntity createUser(String username, String password, List<Role> roles) {
		return txTemplate.execute(status -> {
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbc.update(conn -> {
				PreparedStatement ps = conn.prepareStatement(
						INSERT_USER, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, username);
				ps.setString(2, password);
				return ps;
			}, keyHolder);
			int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
			for (Role r : roles) {
				jdbc.update(
						"INSERT INTO users_to_roles (user_id, role_id) " +
								"VALUES (?, ?)", id, r.id);
			}
			return new UserEntity(id, username, password, roles, 1, BigDecimal.ZERO);
		});
	}

	@Override
	public UserEntity getUser(int userId) {
		return txTemplate.execute(status -> {
			Map<String, Object> user = jdbc.queryForMap(
					"SELECT id, username, password_hash, image_id, balance " +
							"FROM users WHERE id = ?", userId);
			// TODO: Throw exception when creds not found, but dunno what exception
			// is returned. Need to test this.

			List<Integer> roleIDs = jdbc.query(
					"SELECT role_id FROM users_to_roles WHERE user_id = ?",
					(rs, rowNum) -> {
						return rs.getInt("role_id");
					}, user.get("id"));

			List<Role> roles = roleIDs.stream().
					map((id) -> switch (id) {
						case 1 -> Role.USER;
						case 2 -> Role.ADMIN;
						default -> throw new RuntimeException("invalid role id");
					}).
					collect(Collectors.toList());
			return (
					new UserEntity(
							(int) user.get("id"),
							(String) user.get("username"),
							(String) user.get("password_hash"),
							roles,
							(int) user.get("image_id"),
							(BigDecimal) user.get("balance")));
		});
	}

	@Override
	public void deleteUser(int id) {
		txTemplate.execute(status -> {
			jdbc.update("DELETE FROM users " +
					"WHERE id = ?", id);
			jdbc.update("DELETE FROM user_groups " +
					"WHERE user_id = ?", id);
			jdbc.update("DELETE FROM comments " +
					"WHERE user_id = ?", id);
			jdbc.update("DELETE FROM posts " +
					"WHERE user_id = ?", id);
			return null;
		});
	}

	@Override
	public BigDecimal frozenAssetsInGroup(int id, Integer groupId) {
		return jdbc.query("SELECT frozen_assets " +
						"FROM user_groups " +
						"WHERE user_id = ? " +
						"AND group_id = ?",
				rs -> {
					if (rs.next()) {
						return rs.getBigDecimal("frozen_assets");
					}
					return null;
				},
				id, groupId);
	}

	@Override
	public List<UserEntity> usersWithoutPost(int groupId) {
		List<Integer> ids =  jdbc.query("SELECT user_groups.user_id " +
						"FROM user_groups " +
						"INNER JOIN fitGroups " +
						"ON fitGroups.id = user_groups.group_id " +
						"WHERE fitGroups.id = ?",
				(rs, rowNum) -> rs.getInt("user_id"), groupId);

		List<UserEntity> users = new ArrayList<>();
		for (int id: ids) {
			users.add(getUser(id));
		}
		return users;
	}

	@Override
	public void changeBalance(int userId, BigDecimal money) {
		//TODO: Throw exception when balance + money < 0 or > 999999.99
		jdbc.update(
				"UPDATE users SET balance = balance + ? WHERE id = ? " +
						"VALUES (?, ?)", money, userId);
	}

	@Override
	public void changeGroupFrozenAssets(int userId, BigDecimal money, int groupId) {
		jdbc.update(
				"UPDATE user_groups " +
						"SET frozen_assets = ? " +
						"WHERE user_id = ? AND group_id = ?", money, userId, groupId);
	}

	@Override
	public void changeAvatar(int userId, int image_id) {
		jdbc.update(
				"UPDATE users SET image-id = ? WHERE id = ? " +
						"VALUES (?, ?)", image_id, userId);
	}

	@Override
	public void changePassword(int userId, String newPasswordHash) {
		//TODO: check if the new password is the same as the old password
		jdbc.update(
				"UPDATE users SET password_hash = ? WHERE id = ? " +
						"VALUES (?, ?)", newPasswordHash, userId);
	}

	@Override
	public List<String> getUserMemories(int userId) {
		List<String> memories = jdbc.query(
				"SELECT image_id FROM posts WHERE user_id = ?",
				(rs, rowNum) -> {
					return rs.getString("image_id");
				}, userId);
		return memories;
	}

	@Override
	public void addUserToGroup(int userId, int groupId, BigDecimal frozenAssets) {
		txTemplate.execute(status -> {
			jdbc.update("INSERT INTO user_groups (user_id, group_id, frozen_assets) VALUES (?, ?, ?)",
					userId, groupId, frozenAssets);
			jdbc.update("INSERT INTO group_post (group_id, post_id) " +
					"SELECT group_id, post_id " +
					"FROM users_to_groups " +
					"INNER JOIN posts ON users_to_groups.user_id = posts.user_id");
			return null;
		});
	}

	@Override
	public void removeUserFromGroup(int userId, int groupId, BigDecimal frozenAssets) {
		txTemplate.execute(status -> {
			jdbc.update("DELETE FROM user_groups " +
							"WHERE user_id = ? " +
							"AND group_id = ?",
					userId, groupId);
			jdbc.update("UPDATE users " +
					"SET balance = balance + ? " +
					"WHERE id = ?", frozenAssets, userId);
			jdbc.update("DELETE FROM group_post " +
					"WHERE user_id = ? " +
					"AND group_id = ?", userId, groupId);
			return null;
		});
	}

	@Override
	public String createAuthToken(Integer userId, String authToken) {
		jdbc.update("INSERT INTO auth_tokens (token, user_id) " +
				"VALUES (?, ?)", authToken, userId);
		return authToken;
	}

	@Override
	public Optional<UserEntity> getUser(String username) {
		return txTemplate.execute(status -> {
			Map<String, Object> user = jdbc.queryForMap(
					"SELECT id, username, password_hash, image_id, balance " +
							"FROM users WHERE username = ?", username);

			List<Integer> roleIDs = jdbc.query(
					"SELECT role_id FROM users_to_roles WHERE user_id = ?",
					(rs, rowNum) -> {
						return rs.getInt("role_id");
					}, user.get("id"));

			List<Role> roles = roleIDs.stream().
					map((id) -> switch (id) {
						case 1 -> Role.USER;
						case 2 -> Role.ADMIN;
						default -> throw new RuntimeException("invalid role id");
					}).
					collect(Collectors.toList());
			return Optional.of(
					new UserEntity(
							(int) user.get("id"),
							(String) user.get("username"),
							(String) user.get("password_hash"),
							roles,
							(int) user.get("image_id"),
							(BigDecimal) user.get("balance")));
		});
	}

	@Override
	public Optional<UserEntity> getUserByAuthToken(String authToken) {
		return txTemplate.execute(status -> {
			Map<String, Object> user = jdbc.queryForMap(
					"SELECT u.id as id," +
							" u.username as username," +
							" u.password_hash as password_hash," +
							" u.image_id as image_id," +
							" u.balance as balance " +
							"FROM users u " +
							"JOIN auth_tokens at ON u.id = at.user_id " +
							"WHERE at.token = ?", authToken);

			List<Integer> roleIDs = jdbc.query(
					"SELECT role_id FROM users_to_roles WHERE user_id = ?",
					(rs, rowNum) -> {
						return rs.getInt("role_id");
					}, user.get("id"));

			List<Role> roles = roleIDs.stream().
					map((id) -> switch (id) {
						case 1 -> Role.USER;
						case 2 -> Role.ADMIN;
						default -> throw new RuntimeException("invalid role id");
					}).
					collect(Collectors.toList());
			return Optional.of(
					new UserEntity(
							(int) user.get("id"),
							(String) user.get("username"),
							(String) user.get("password_hash"),
							roles,
							(int) user.get("image_id"),
							(BigDecimal) user.get("balance")));
		});
	}

	static class Queries {
		public static final String INSERT_USER =
				"INSERT INTO users (username, password_hash) VALUES (?, ?)";
		public static final String INSERT_USER_TO_ROLE =
				"INSERT INTO users_to_roles (user_id, role_id) VALUES (?, ?)";
		public static final String GET_BALANCE_BY_ID =
				"SELECT balance FROM users WHERE id=?";
	}
}


