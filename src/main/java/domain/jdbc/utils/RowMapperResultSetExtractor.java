package domain.jdbc.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowMapperResultSetExtractor<T> implements ResultSetExtractor<List<T>> {
	private final RowMapper<T> mapper;

	public RowMapperResultSetExtractor(RowMapper<T> mapper) {
		this.mapper = mapper;
	}


	@Override
	public List<T> extractData(ResultSet resultSet) throws SQLException {
		List<T> results = new ArrayList<>();
		while (resultSet.next()) {
			results.add(this.mapper.mapRow(resultSet));
		}
		return results;
	}
}
