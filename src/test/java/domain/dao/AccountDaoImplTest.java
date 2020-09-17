package domain.dao;

import domain.jdbc.JdbcTemplate;
import model.Account;
import org.junit.Before;
import org.junit.Test;

import static domain.dao.AccountDao.SQL_FIND_BY_NAME;
import static domain.dao.AccountDao.SQL_UPDATE;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class AccountDaoImplTest {
	private JdbcTemplate jdbcTemplate;
	private AccountDaoImpl accountDao;

	@Before
	public void setup() {
		jdbcTemplate = mock(JdbcTemplate.class);
		accountDao = new AccountDaoImpl(jdbcTemplate);
	}

	@Test
	public void whenFindByNameExistingAccountThenReturnIt() {
		Account expected = mock(Account.class);
		String name = "Name";
		when(jdbcTemplate.findForObject(eq(SQL_FIND_BY_NAME),
				eq(new Object[]{name}), anyObject())).thenReturn(expected);
		Account actual = accountDao.findByName(name);
		assertSame(expected, actual);
		verify(jdbcTemplate)
				.findForObject(eq(SQL_FIND_BY_NAME), eq(new Object[]{name}), anyObject());
	}

	@Test
	public void whenUpdateExistingAccountThenUpdateIt() {
		Account account = mock(Account.class);
		accountDao.update(account);
		verify(jdbcTemplate).update(SQL_UPDATE, account);
	}

}
