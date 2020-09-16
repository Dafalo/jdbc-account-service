package domen.jdbc;

public class JdbcAccess {
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	protected DataSource obtainDataSource() {
		DataSource dataSource = getDataSource();
		if (dataSource == null) {
			throw new IllegalStateException("No DataSource set");
		}
		return dataSource;
	}
}
