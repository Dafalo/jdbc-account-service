package domain.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataSource {
	private final String url;
	private final Properties conProperties;

	public DataSource(String url, Properties properties) {
		this.url = url;
		this.conProperties = new Properties();
		conProperties.putAll(properties);
	}

	public Connection getConnection() {
		try {
			return DriverManager.getConnection(url, conProperties);
		} catch (SQLException e) {
			throw new RuntimeException(e);// TODO create exception
		}
	}

}
