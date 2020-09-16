package domen.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class JdbcUtils {

	public static Connection getConnection(DataSource dataSource) {
		if (dataSource == null) {
			throw new IllegalArgumentException("No DataSource specified");
		}
		Connection connection = dataSource.getConnection();
		if (connection == null) {
			throw new IllegalStateException("DataSource returned null:" + dataSource);
		}
		return connection;

	}

	public static void closeStatement(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	public static void closeResultSet( ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException ex) {
//				logger.trace("Could not close JDBC ResultSet", ex);
			}
			catch (Throwable ex) {

//				logger.trace("Unexpected exception on closing JDBC ResultSet", ex);
			}
		}
	}
}
