import domain.dao.AccountDao;
import domain.dao.AccountDaoImpl;
import domain.jdbc.DataSource;
import domain.jdbc.JdbcTemplate;
import domain.jdbc.JdbcWrapper;
import exception.DataAccessException;
import model.Account;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class JdbcApiTest {
	private static AccountDao accountDao;
	private static JdbcTemplate jdbcTemplate;

	@BeforeClass
	public static void setup() throws ClassNotFoundException {
		Properties properties = new Properties();
		properties.setProperty("user", "sa");
		properties.setProperty("password", "");
		DataSource dataSource = new DataSource("jdbc:h2:~/test;MODE=PostgreSQL", properties);
		JdbcWrapper jdbcWrapper = new JdbcWrapper();
		jdbcTemplate = new JdbcTemplate(dataSource, jdbcWrapper);
		accountDao = new AccountDaoImpl(jdbcTemplate);
		Class.forName("org.h2.Driver");
		jdbcTemplate.createTable("CREATE TABLE IF NOT EXISTS account (\n" +
				"    first_name VARCHAR (50) PRIMARY KEY,\n" +
				"\tlast_name VARCHAR ( 50 )  NOT NULL\n" +
				");");

	}

	@Before
	public void clear() {
		jdbcTemplate.execute("DELETE FROM account", PreparedStatement::execute);
	}

	@Test
	public void whenFindByNameExistingAccountThenReturnIt() {
		Account expected = getAccount();
		accountDao.insert(expected);
		Account actual = accountDao.findByName("Adam");
		assertEquals(expected, actual);
	}

	@Test
	public void whenFindByNameNotExistingAccountThenThrowException() {
		try {
			accountDao.findByName("Lokk");
		} catch (Exception ex) {
			assertTrue(ex instanceof DataAccessException);
			assertEquals(ex.getMessage(), "Data is empty");
			return;
		}
		fail("Exception is expected");
	}

	private Account getAccount() {
		Account account = new Account();
		account.setFirstName("Adam");
		account.setLastName("Lambert");
		return account;
	}
}
