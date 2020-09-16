package domen.dao;

import domen.jdbc.JdbcTemplate;
import model.Account;

public class AccountDaoImpl implements AccountDao {

	private final JdbcTemplate jdbcTemplate;

	public AccountDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Account findByName(String name) {
		return jdbcTemplate.findForObject(SQL_FIND_BY_NAME, new Object[]{name}, (rs, rowNum) -> {
			Account account = new Account();
			account.setFirstName(rs.getString(AccountDao.FIRST_NAME_COLUMN));
			account.setLastName(rs.getString(AccountDao.LAST_NAME_COLUMN));
			return account;
		});
	}

	public void insert(Account account) {
		jdbcTemplate.update(SQL_INSERT, account.getFirstName(), account.getLastName());
	}

	public void update(Account account) {
		jdbcTemplate.update(SQL_INSERT, account.getFirstName(), account.getLastName());
	}
}
