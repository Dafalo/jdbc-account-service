package domain.jdbc;

import domain.jdbc.utils.PreparedStatementCallback;
import domain.jdbc.utils.ResultSetExtractor;
import domain.jdbc.utils.RowMapper;
import domain.jdbc.utils.RowMapperResultSetExtractor;
import exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class JdbcTemplate {

	private final DataSource dataSource;
	private final JdbcWrapper jdbcWrapper;

	public JdbcTemplate(DataSource dataSource, JdbcWrapper jdbcWrapper) {
		this.dataSource = dataSource;
		this.jdbcWrapper = jdbcWrapper;
	}

	public void createTable(String sql) {
		execute(sql, PreparedStatement::execute);
	}

	public <T> T findForObject(String sql, Object[] args, RowMapper<T> rowMapper) {
		List<T> results = query(sql, args, new RowMapperResultSetExtractor<>(rowMapper));
		return nullableSingleResult(results);
	}

	public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse) {
		if (rse == null) {
			throw new DataAccessException("ResultSetExtractor is null");
		}
		return execute(sql, ps -> {
			ResultSet rs = null;
			try {
				setValues(ps, args);
				rs = ps.executeQuery();
				return rse.extractData(rs);
			} finally {
				jdbcWrapper.closeResultSet(rs);
			}
		});
	}


	private <T> T nullableSingleResult(Collection<T> results) {
		if (results.isEmpty()) {
			throw new DataAccessException("Data is empty");
		}
		if (results.size() > 1) {
			throw new DataAccessException("Input data has size > 1");
		}
		return results.iterator().next();
	}

	public int update(String sql, Object... args) {
		return execute(sql, ps -> {
			setValues(ps, args);
			return ps.executeUpdate();
		});
	}

	public <T> T execute(String sql, PreparedStatementCallback<T> action) {
		Connection connection = jdbcWrapper.getConnection(dataSource);
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			return action.doInPreparedStatement(ps);
		} catch (SQLException ex) {
			throw new DataAccessException(ex);
		} finally {
			jdbcWrapper.closeStatement(ps);
			jdbcWrapper.closeConnection(connection);
		}
	}

	private void setValues(PreparedStatement ps, Object... args) {
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				Object arg = args[i];
				doSetValue(ps, i + 1, arg);
			}
		}
	}

	private void doSetValue(PreparedStatement ps, int i, Object arg) {
		try {
			ps.setObject(i, arg);
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

}
