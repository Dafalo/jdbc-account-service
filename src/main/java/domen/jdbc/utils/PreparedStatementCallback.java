package domen.jdbc.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementCallback<T> {
	T doInPreparedStatement(PreparedStatement ps) throws SQLException;
}
