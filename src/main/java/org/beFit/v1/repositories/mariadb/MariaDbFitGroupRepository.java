package org.beFit.v1.repositories.mariadb;

import org.beFit.v1.repositories.FitGroupRepository;
import org.beFit.v1.repositories.entities.FitGroupEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class MariaDbFitGroupRepository implements FitGroupRepository {
	private final TransactionTemplate txTemplate;
	private final JdbcTemplate jdbc;

	public MariaDbFitGroupRepository(TransactionTemplate txTemplate, JdbcTemplate connection) {
		this.txTemplate = txTemplate;
		this.jdbc = connection;
	}

	@Override
	public FitGroupEntity createFitGroup(String name, BigDecimal stake) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbc.update(conn -> {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO fitGroups (group_name, stake)" +
							"VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, name);
			ps.setBigDecimal(2, stake);
			return ps;
		}, keyHolder);

		Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
		return new FitGroupEntity(id, name, stake, BigDecimal.ZERO);
	}

	@Override
	public FitGroupEntity getFitGroup(int id) {
		return jdbc.queryForObject("SELECT id, " +
						"group_name, " +
						"stake, " +
						"balance " +
						"FROM fitGroups " +
						"WHERE id = ?",
				(rs, rowNum) -> fromResultSet(rs), id);
	}

	@Override
	public void deleteFitGroup(int id) {
		txTemplate.execute(status -> {
			jdbc.update("DELETE FROM fitGroups " +
					"WHERE id = ?", id);
			jdbc.update("DELETE FROM user_groups " +
					"WHERE group_id = ?", id);
			return null;
		});
	}

	private FitGroupEntity fromResultSet(ResultSet rs) throws SQLException {
		return new FitGroupEntity(
				rs.getInt("id"),
				rs.getString("name"),
				rs.getBigDecimal("stake"),
				rs.getBigDecimal("balance")
		);
	}
}
