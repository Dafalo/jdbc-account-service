package domain.jdbc.utils;


import domain.jdbc.DataSource;
import domain.jdbc.JdbcTemplate;
import domain.jdbc.JdbcWrapper;
import exception.DataAccessException;
import model.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static domain.dao.AccountDao.SQL_FIND_BY_NAME;
import static domain.dao.AccountDao.SQL_INSERT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JdbcTemplateTest {

	private JdbcTemplate jdbcTemplate;

	private Connection conn;
	private PreparedStatement ps;
	private DataSource dataSource;
	private JdbcWrapper jdbcWrapper;
	@SuppressWarnings("unchecked")
	private PreparedStatementCallback<Account> psc =
			(PreparedStatementCallback<Account>) mock(PreparedStatementCallback.class);

	@Before
	public void setUp() {
		conn = mock(Connection.class);
		ps = mock(PreparedStatement.class);
		dataSource = mock(DataSource.class);
		jdbcWrapper = mock(JdbcWrapper.class);
		jdbcTemplate = new JdbcTemplate(dataSource, jdbcWrapper);

	}

	@Test
	public void whenUpdateCallSetObject() throws SQLException {
		Account account = getAccount();
		when(jdbcWrapper.getConnection(dataSource)).thenReturn(conn);
		when(conn.prepareStatement(anyString())).thenReturn(ps);

		doNothing().when(ps).setObject(1, account.getFirstName());

		jdbcTemplate.update(SQL_INSERT, account.getFirstName(), account.getLastName());

		verify(ps).setObject(1, account.getFirstName());
	}

	@Test
	public void execute() throws SQLException {
		when(jdbcWrapper.getConnection(dataSource)).thenReturn(conn);
		when(conn.prepareStatement(SQL_INSERT)).thenReturn(ps);
		Account expected = getAccount();
		when(psc.doInPreparedStatement(ps)).thenReturn(expected);

		Account actual = jdbcTemplate.execute(SQL_INSERT, psc);

		assertSame(expected, actual);
		verify(psc).doInPreparedStatement(ps);
		verify(jdbcWrapper).closeConnection(conn);
		verify(jdbcWrapper).closeStatement(ps);
	}

	@Test
	public void findAccountByName() throws SQLException {
		Account account = getAccount();
		ResultSet resultSet = mock(ResultSet.class);
		when(jdbcWrapper.getConnection(dataSource)).thenReturn(conn);
		when(conn.prepareStatement(anyString())).thenReturn(ps);
		when(ps.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true, false);

		Account actual = jdbcTemplate.findForObject(SQL_FIND_BY_NAME,
				new Object[]{account.getFirstName()},
				(rs) -> account);
		assertSame(account, actual);
	}

	@Test
	public void whenCallQueryReturnedNullListShouldThrowException() throws SQLException {
		ResultSet resultSet = mock(ResultSet.class);
		when(jdbcWrapper.getConnection(dataSource)).thenReturn(conn);
		when(conn.prepareStatement(anyString())).thenReturn(ps);
		when(ps.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true, true, false);

		try {
			jdbcTemplate.findForObject(SQL_FIND_BY_NAME,
					new Object[]{"Name"},
					(rs) -> mock(Account.class));
		} catch (Exception ex) {
			assertTrue(ex instanceof DataAccessException);
			assertEquals(ex.getMessage(), "Input data has size > 1");
			return;
		}
		fail("Exception was expected");
	}

	@Test
	public void whenCallQueryReturnedEmptyListShouldThrowException() throws SQLException {
		ResultSet resultSet = mock(ResultSet.class);
		ResultSetExtractor rse = mock(ResultSetExtractor.class);
		when(jdbcWrapper.getConnection(dataSource)).thenReturn(conn);
		when(conn.prepareStatement(anyString())).thenReturn(ps);
		when(ps.executeQuery()).thenReturn(resultSet);
		when(rse.extractData(resultSet)).thenReturn(new ArrayList<>());

		try {
			jdbcTemplate.findForObject(SQL_FIND_BY_NAME,
					new Object[]{"Name"},
					(rs) -> mock(Account.class));
		} catch (Exception ex) {
			assertTrue(ex instanceof DataAccessException);
			assertEquals(ex.getMessage(), "Data is empty");
			return;
		}
		fail("Exception was expected");
	}

	@Test
	public void whenCallExecuteAndCatchSQLExceptionShouldCallCloseMethods() throws SQLException {
		when(jdbcWrapper.getConnection(dataSource)).thenReturn(conn);
		SQLException expected = new SQLException();
		when(conn.prepareStatement(SQL_INSERT)).thenThrow(expected);
		Exception exception = null;
		try {
			jdbcTemplate.execute(SQL_INSERT, psc);
		} catch (DataAccessException ex) {
			exception = ex;
		}
		assertNotNull(exception);
		assertSame(expected, exception.getCause());
		verify(jdbcWrapper, times(1)).closeConnection(conn);
		verify(jdbcWrapper, times(1)).closeStatement(null);
	}


	private Account getAccount() {
		Account account = new Account();
		account.setFirstName("Adam");
		account.setLastName("Lambert");
		return account;
	}
}
