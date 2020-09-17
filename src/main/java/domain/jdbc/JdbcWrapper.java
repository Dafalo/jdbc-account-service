package domain.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JdbcWrapper {

	public Connection getConnection(DataSource dataSource) {
		return JdbcUtils.getConnection(dataSource);
	}

	public void closeStatement(PreparedStatement statement) {
		JdbcUtils.closeStatement(statement);
	}

	public void closeConnection(Connection connection) {
		JdbcUtils.closeConnection(connection);
	}

	public void closeResultSet(ResultSet rs) {
		JdbcUtils.closeResultSet(rs);
	}
}
