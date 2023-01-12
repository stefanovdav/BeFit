package org.beFit.v1.repositories.mariadb;

import org.beFit.v1.repositories.CommentRepository;
import org.beFit.v1.repositories.entities.CommentEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MariaDbCommentRepository implements CommentRepository {
	private final JdbcTemplate jdbc;
	private final TransactionTemplate txTemplate;

	public MariaDbCommentRepository(
			JdbcTemplate jdbc, TransactionTemplate txTemplate) {
		this.jdbc = jdbc;
		this.txTemplate = txTemplate;
	}

	@Override
	public CommentEntity createFirstComment(String content, int userId, int post_id) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(conn -> {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO comments (user_id, content, post_id) " +
							"VALUES (?, ?, ?);",
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, userId);
			ps.setString(2, content);
			ps.setInt(3, post_id);
			return ps;
		}, keyHolder);

		Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
		return new CommentEntity(id, post_id,  userId, content, 0, null);
	}

	@Override
	public CommentEntity createSubComment(String content, int parentId, int userId) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		CommentEntity comment = getComment(parentId);
		int post_id = getComment(parentId).post_id;
		jdbc.update(conn -> {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO comments (user_id, content, parent_comment_id, post_id, depth) " +
							"VALUES (?, ?, ?, ?, ?);",
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, userId);
			ps.setString(2, content);
			ps.setInt(3, parentId);
			ps.setInt(4, comment.post_id);
			ps.setInt(5, comment.depth + 1);
			return ps;
		}, keyHolder);

		Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
		return new CommentEntity(id, post_id, userId, content, 0, null);

	}

	@Override
	public CommentEntity getComment(int commentId) {
		return jdbc.queryForObject("SELECT id, " +
						"post_id, " +
						"user_id, " +
						"content, " +
						"depth, " +
						"parent_comment_id " +
						"FROM comments " +
						"WHERE id = ?",
				(rs, rowNum) -> fromResultSet(rs), commentId);
	}

	@Override
	public Optional<CommentEntity> getParent(int commentId) {
		return Optional.ofNullable(getComment(getComment(commentId).parentId));
	}

	@Override
	public Optional<List<CommentEntity>> getChildren(int commentId) {
		return Optional.of(jdbc.query("SELECT id, " +
						"post_id " +
						"user_id, " +
						"content, " +
						"depth, " +
						"parent_comment_id " +
						"FROM comments " +
						"WHERE parent_comment_id = ?",
				(rs, rowNum) -> fromResultSet(rs), commentId));
	}

	@Override
	public void editContent(int commentId, String newContent) {
		jdbc.update(
				"UPDATE comments SET content = ? WHERE id = ? ",
				 newContent, commentId);
	}

	private CommentEntity fromResultSet(ResultSet rs) throws SQLException {
		return new CommentEntity(
				rs.getInt("id"),
				rs.getInt("post_id"),
				rs.getInt("user_id"),
				rs.getString("content"),
				rs.getInt("depth"),
				rs.getInt("parent_comment_id")
		);
	}
}
