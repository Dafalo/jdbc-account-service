import domain.dao.AccountDaoImpl;
import domain.jdbc.DataSource;
import domain.jdbc.JdbcTemplate;
import domain.jdbc.JdbcUtils;
import domain.jdbc.JdbcWrapper;
import mapper.AccountMapper;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import services.AccountService;
import servlet.AccountServlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppContext {

	public void init() throws Exception {
		Properties properties = new Properties();
		setProperties(properties);
		DataSource dataSource = new DataSource(properties.getProperty("url"), properties);
		JdbcUtils jdbcUtils = new JdbcUtils();
		JdbcWrapper jdbcWrapper = new JdbcWrapper();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource, jdbcWrapper);
		AccountMapper accountMapper = new AccountMapper();
		AccountDaoImpl accountDao = new AccountDaoImpl(jdbcTemplate);
		AccountService accountService = new AccountService(accountDao, accountMapper);
		jdbcTemplate.createTable("CREATE TABLE IF NOT EXISTS account (\n" +
				"    first_name VARCHAR (50) PRIMARY KEY,\n" +
				"\tlast_name VARCHAR ( 50 )  NOT NULL\n" +
				");");
		Server server = new Server(8080);
		ServletContextHandler context = new ServletContextHandler(server, "/account");

		AccountServlet accountServlet = new AccountServlet(accountService);
		context.addServlet(new ServletHolder(accountServlet), "/");

		server.start();
		server.join();

	}

	private void setProperties(Properties properties) {
		try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
