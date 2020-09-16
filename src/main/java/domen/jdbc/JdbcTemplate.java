package domen.jdbc;

import domen.jdbc.utils.PreparedStatementCallback;
import domen.jdbc.utils.ResultSetExtractor;
import domen.jdbc.utils.RowMapper;
import domen.jdbc.utils.RowMapperResultSetExtractor;
import exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class JdbcTemplate {

	private final JdbcAccess jdbcAccess;

	public JdbcTemplate(JdbcAccess jdbcAccess) {
		this.jdbcAccess = jdbcAccess;
	}

	public void createTable(String sql) {
		Connection connection = JdbcUtils.getConnection(jdbcAccess.obtainDataSource());
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.execute();
		} catch (SQLException ex) {
			JdbcUtils.closeStatement(ps);
			ps = null;
			JdbcUtils.closeConnection(connection);
			connection = null;
			throw new DataAccessException("", ex);
		} finally {
			JdbcUtils.closeStatement(ps);
			JdbcUtils.closeConnection(connection);
		}
	}

	public <T> T findForObject(String sql, Object[] args, RowMapper<T> rowMapper) {
		List<T> results = query(sql, args, new RowMapperResultSetExtractor<>(rowMapper, 1));
		return nullableSingleResult(results);
	}

	public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse) {
		if (rse == null) {
//			throw  new
		}
		return execute(sql, args, ps -> {
			ResultSet rs = null;
			try {
				setValues(ps, args);
				rs = ps.executeQuery();
				return rse.extractData(rs);
			} finally {
				JdbcUtils.closeResultSet(rs);
			}
		});
	}


	public static <T> T nullableSingleResult(Collection<T> results) {
		if (results.isEmpty()) {
			throw new DataAccessException("Data is empty");
		}
		if (results.size() > 1) {
			throw new DataAccessException("Size > 1");
		}
		return results.iterator().next();
	}

	public int update(String sql, Object... args) {
		return execute(sql, args, ps -> {
			setValues(ps, args);
			return ps.executeUpdate();
		});
	}

	public <T> T execute(String sql, Object[] args, PreparedStatementCallback<T> action) {
		Connection connection = JdbcUtils.getConnection(jdbcAccess.obtainDataSource());
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			setValues(ps, args);
			T result = action.doInPreparedStatement(ps);
			return result;
		} catch (SQLException ex) {
			JdbcUtils.closeStatement(ps);
			ps = null;
			JdbcUtils.closeConnection(connection);
			connection = null;
			throw new DataAccessException("", ex);
		} finally {
			JdbcUtils.closeStatement(ps);
			JdbcUtils.closeConnection(connection);
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
			throw new RuntimeException();
		}
	}

}
