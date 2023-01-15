package org.beFit.v1.repositories.mariadb;

import org.beFit.v1.repositories.FitGroupRepository;
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
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class MariaDbFitGroupRepository implements FitGroupRepository {
	private final TransactionTemplate txTemplate;
	private final JdbcTemplate jdbc;

	public MariaDbFitGroupRepository(TransactionTemplate txTemplate, JdbcTemplate connection) {
		this.txTemplate = txTemplate;
		this.jdbc = connection;
	}

	@Override
	public FitGroupEntity createFitGroup(String name, BigDecimal stake, Integer tax, Date end, String key) {
		Date now = Date.valueOf(LocalDate.now());
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbc.update(conn -> {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO fitGroups " +
							"(group_name, stake, tax, end, group_key, start)" +
							"VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, name);
			ps.setBigDecimal(2, stake);
			ps.setInt(3, tax);
			ps.setDate(4, end);
			ps.setString(5, key);
			ps.setDate(6, now);
			return ps;
		}, keyHolder);

		Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();

		return new FitGroupEntity(id, name, stake, tax, now, end, 0);
	}

	@Override
	public FitGroupEntity getFitGroup(int id) {
		return jdbc.queryForObject("SELECT id, " +
						"group_name, " +
						"stake, " +
						"participants, " +
						"tax, " +
						"start, " +
						"end " +
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

	@Override
	public List<FitGroupEntity> listGroups() {
		return jdbc.query("SELECT fitGroups.id," +
						" fitGroups.group_name," +
						" fitGroups.stake," +
						" fitGroups.tax," +
						" fitGroups.participants," +
						" fitGroups.group_key," +
						" fitGroups.start," +
						" fitGroups.end " +
						"FROM fitGroups",
				(rs, rowNum) -> fromResultSet(rs));
	}

	private FitGroupEntity fromResultSet(ResultSet rs) throws SQLException {
		return new FitGroupEntity(
				rs.getInt("id"),
				rs.getString("name"),
				rs.getBigDecimal("stake"),
				rs.getInt("tax"),
				rs.getDate("start"),
				rs.getDate("end"),
				rs.getInt("participants"));
	}
}
