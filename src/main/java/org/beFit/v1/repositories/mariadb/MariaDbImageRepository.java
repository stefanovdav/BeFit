package org.beFit.v1.repositories.mariadb;

import org.beFit.v1.repositories.ImageRepository;
import org.beFit.v1.repositories.entities.ImageEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class MariaDbImageRepository implements ImageRepository {

	private final JdbcTemplate jdbc;
	private final TransactionTemplate txTemplate;

	public MariaDbImageRepository(JdbcTemplate jdbc, TransactionTemplate txTemplate) {
		this.jdbc = jdbc;
		this.txTemplate = txTemplate;
	}

	@Override
	public ImageEntity create(String title, String url, String publicId) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(conn -> {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO images (title, url, public_id) " + "VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, title);
			ps.setString(2, url);
			ps.setString(3, publicId);
			return ps;
		}, keyHolder);

		Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
		return new ImageEntity(id, title, url, publicId);
	}

	@Override
	public ImageEntity get(Integer id) {
		return jdbc.queryForObject("SELECT id, " +
						"title, " +
						"url, " +
						"public_id " +
						"FROM images " +
						"WHERE id = ?",
				(rs, rowNum) -> fromResultSet(rs), id);
	}

	@Override
	public void delete(Integer id) {
		jdbc.update("DELETE FROM images WHERE id = ?;", id);
	}

	private ImageEntity fromResultSet(ResultSet rs) throws SQLException {
		return new ImageEntity(
				rs.getInt("id"),
				rs.getString("title"),
				rs.getString("url"),
				rs.getString("public_id"));
	}
}
