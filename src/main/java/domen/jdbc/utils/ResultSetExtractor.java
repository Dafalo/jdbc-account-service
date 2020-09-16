package domen.jdbc.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetExtractor<T> {
	T extractData(ResultSet resultSet) throws SQLException;
}
