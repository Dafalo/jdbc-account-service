package domen.jdbc.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowMapperResultSetExtractor<T> implements ResultSetExtractor<List<T>> {
	private final RowMapper<T> mapper;
	private final int rows;

	public RowMapperResultSetExtractor(RowMapper<T> mapper, int rows) {
		this.mapper = mapper;
		this.rows = rows;
	}


	@Override
	public List<T> extractData(ResultSet resultSet) throws SQLException {
		List<T> results = this.rows > 0 ? new ArrayList<>(this.rows) : new ArrayList<>();
		int rowNum = 0;
		while (resultSet.next()) {
			results.add(this.mapper.mapRow(resultSet, rowNum++));
		}
		return results;
	}
}
