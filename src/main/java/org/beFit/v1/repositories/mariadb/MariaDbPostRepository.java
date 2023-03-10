package org.beFit.v1.repositories.mariadb;

import org.beFit.v1.repositories.PostRepository;
import org.beFit.v1.repositories.entities.PostEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

//TODO: RabbitMQ when creating post, set delete time in payload in RabbitMQ
// then it does microservice to archive it
public class MariaDbPostRepository implements PostRepository {
	private final TransactionTemplate txTemplate;
	private final JdbcTemplate jdbc;

	public MariaDbPostRepository(TransactionTemplate txTemplate, JdbcTemplate jdbc) {
		this.txTemplate = txTemplate;
		this.jdbc = jdbc;
	}

	@Override
	public PostEntity createPost(int userId, Integer imageId, String content) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(conn -> {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO posts (user_id, image_id, content) VALUES (?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, userId);
			ps.setInt(2, imageId);
			ps.setString(3, content);
			return ps;
		}, keyHolder);

		Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
		return new PostEntity(id, userId, imageId, content, 0, Instant.now(), false);
	}

	@Override
	public PostEntity getById(int postId) {
		return jdbc.queryForObject("SELECT " +
						"id, " +
						"user_id, " +
						"image_id, " +
						"content, " +
						"votes, " +
						"post_time, " +
						"is_archived " +
						"FROM posts " +
						"WHERE id = ?",
				(rs, rowNum) -> fromResultSet(rs), postId);
	}

	@Override
	public void archivePost(int postId) {
		jdbc.update("UPDATE posts " +
				"SET is_archived = TRUE " +
				"WHERE id = ?", postId);
	}

	@Override
	public void changeVotes(int postId, int vote) {
		jdbc.update(
				"UPDATE posts SET votes = votes + ? WHERE id = ? ", vote, postId);
	}

	@Override
	public List<PostEntity> getPostsByUser(int page, int pageSize, int userId) {
		return jdbc.query("SELECT " +
						"id, " +
						"user_id, " +
						"image_id, " +
						"content, votes, " +
						"post_time, " +
						"is_archived " +
						"FROM posts " +
						"WHERE user_id = ? " +
						" ORDER BY p.post_time DESC " +
						"LIMIT ? " +
						"OFFSET ?",
				(rs, rowNum) -> fromResultSet(rs), userId, pageSize, page * pageSize);
	}

	@Override
	public List<PostEntity> listFitGroupsPostsOfUser(int page, int pageSize, int userId) {
		//TODO: optimize to cursor pagination
		return jdbc.query("SELECT p.id, p.user_id, p.image_id, p.content, p.votes, p.post_time, p.is_archived " +
						"FROM posts p " +
						"JOIN user_groups ug " +
						"    ON ug.user_id = p.user_id" +
						"JOIN group_post gp " +
						"    ON gp.post_id = p.id " +
						"WHERE ug.user_id = <user_id> " +
						"AND gp.group_id = <group_id> " +
						"AND p.is_archived = true " +
						"LIMIT ? " +
						"OFFSET ?",
				(rs, rowNum) -> fromResultSet(rs), userId, page * pageSize, pageSize);
	}

	private PostEntity fromResultSet(ResultSet rs) throws SQLException {
		return new PostEntity(
				rs.getInt("id"),
				rs.getInt("user_id"),
				rs.getInt("image_id"),
				rs.getString("content"),
				rs.getInt("votes"),
				rs.getTimestamp("post_time").toInstant(),
				rs.getBoolean("is_archived")
		);
	}

}
