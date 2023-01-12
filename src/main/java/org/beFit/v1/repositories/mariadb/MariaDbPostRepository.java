package org.beFit.v1.repositories.mariadb;

import org.beFit.v1.repositories.PostRepository;
import org.beFit.v1.repositories.entities.CommentEntity;
import org.beFit.v1.repositories.entities.PostEntity;
import org.beFit.v1.repositories.entities.UserEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
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
	public PostEntity createPost(int userId, String imageUrl, String content) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(conn -> {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO posts (user_id, imageURL, content) VALUES (?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, userId);
			ps.setString(2, imageUrl);
			ps.setString(3, content);
			return ps;
		}, keyHolder);

		Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
		return new PostEntity(id, userId, imageUrl, content, 0, Instant.now(), false);
	}

	@Override
	public PostEntity getById(int postId) {
		return jdbc.queryForObject("SELECT " +
						"id, " +
						"user_id, " +
						"imageURL, " +
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
	public List<PostEntity> getPostsByUser(int userId) {
		return jdbc.query("SELECT " +
						"id, " +
						"user_id, " +
						"imageURL, " +
						"content, votes, " +
						"post_time, " +
						"is_archived " +
						"FROM posts " +
						"WHERE user_id = ?",
				(rs, rowNum) -> fromResultSet(rs), userId);
	}

	@Override
	public List<PostEntity> listFitGroupsPostsOfUser(int page, int pageSize, int userId) {
		//TODO: fix the page_id so that it remembers
		return jdbc.query("SELECT posts.id, posts.user_id, " +
						"posts.imageURL, posts.content, " +
						"posts.votes, posts.post_time, " +
						"FROM posts p " +
						"INNER JOIN user_groups ug ON ug.user_id = p.user_id " +
						"WHERE ug.group_id IN (" +
						"    SELECT g.id " +
						"    FROM user_groups ug " +
						"    INNER JOIN fitGroups g ON ug.group_id = g.id " +
						"    WHERE ug.user_id = ?" +
						") ORDER BY p.post_time DESC" +
						"LIMIT ? " +
						"OFFSET ?;",
				(rs, rowNum) -> fromResultSet(rs), userId, page * pageSize, pageSize);
	}

	private PostEntity fromResultSet(ResultSet rs) throws SQLException {
		return new PostEntity(
				rs.getInt("id"),
				rs.getInt("user_id"),
				rs.getString("imageURL"),
				rs.getString("content"),
				rs.getInt("votes"),
				rs.getTimestamp("post_time").toInstant(),
				rs.getBoolean("is_archived")
		);
	}

}
