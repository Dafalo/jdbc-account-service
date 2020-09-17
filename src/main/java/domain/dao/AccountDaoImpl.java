package domain.dao;

import domain.jdbc.JdbcTemplate;
import exception.DataAccessException;
import model.Account;

public class AccountDaoImpl implements AccountDao {

	private final JdbcTemplate jdbcTemplate;

	public AccountDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Account findByName(String name) {
		return jdbcTemplate.findForObject(SQL_FIND_BY_NAME, new Object[]{name}, (rs) -> {
			Account account = new Account();
			account.setFirstName(rs.getString(AccountDao.FIRST_NAME_COLUMN));
			account.setLastName(rs.getString(AccountDao.LAST_NAME_COLUMN));
			return account;
		});
	}

	public void insert(Account account) {
		int result = jdbcTemplate.update(SQL_INSERT, account.getFirstName(), account.getLastName());
		if (result == 0) {
			throw new DataAccessException("No rows matched the condition");
		}
	}

	public void update(Account account) {
		int result = jdbcTemplate.update(SQL_UPDATE, account.getLastName(), account.getFirstName());
		if (result == 0) {
			throw new DataAccessException("No rows matched the condition");
		}
	}
}
