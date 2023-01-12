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

	public MariaDbCommentRepository(JdbcTemplate jdbc, TransactionTemplate txTemplate) {
		this.jdbc = jdbc;
		this.txTemplate = txTemplate;
	}

	@Override
	public CommentEntity createFirstComment(String content, int userId, int post_id) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(conn -> {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO comments (user_id, content, post_id) " + "VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, userId);
			ps.setString(2, content);
			ps.setInt(3, post_id);
			return ps;
		}, keyHolder);

		Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
		return new CommentEntity(id, post_id, userId, content, Integer.toString(post_id));
	}

	//TODO:Optimize query
	@Override
	public CommentEntity createSubComment(String content, int parentId, int userId) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		CommentEntity comment = getComment(parentId);
		int post_id = getComment(parentId).post_id;
		jdbc.update(conn -> {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO comments (user_id," +
							" content, " +
							"post_id) " +
							"VALUES (?, ?, ?);",
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, userId);
			ps.setString(2, content);
			ps.setInt(3, comment.post_id);
			return ps;
		}, keyHolder);

		Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
		String path = comment.commentPath + "/" + id;

		jdbc.update(
				"UPDATE comments SET comment_path = ? WHERE id = ? ",
				path, id);

		return new CommentEntity(id, post_id, userId, content, path);

	}

	@Override
	public CommentEntity getComment(int commentId) {
		return jdbc.queryForObject("SELECT id, " +
						"post_id, " +
						"user_id, " +
						"content, " +
						"comment_path" +
						"FROM comments " +
						"WHERE id = ?",
				(rs, rowNum) -> fromResultSet(rs), commentId);
	}

	@Override
	public Optional<CommentEntity> getParent(int commentId) {
		String path = getComment(commentId).commentPath;
		int id = path.charAt(path.length() - 1);
		return Optional.ofNullable(getComment(id));
	}

	@Override
	public Optional<List<CommentEntity>> getChildren(int commentId) {
		return Optional.of(jdbc.query("SELECT id, " +
						"post_id " +
						"user_id, " +
						"content, " +
						"comment_path" +
						"FROM comments " +
						"WHERE comment_path LIKE ?",
				(rs, rowNum) -> fromResultSet(rs), getComment(commentId).commentPath + "/%"));
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
				rs.getString("comment_path"));
	}
}
