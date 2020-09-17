package servlet;

import com.google.gson.Gson;
import dto.AccountDto;
import org.eclipse.jetty.http.HttpStatus;
import services.AccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AccountServlet extends HttpServlet {

	private final AccountService accountService;

	private Gson gson = new Gson();

	public AccountServlet(AccountService accountService) {
		this.accountService = accountService;
	}


	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String first_name = request.getParameter("first_name");
		AccountDto account = accountService.getAccount(first_name);

		String accountJsonString = this.gson.toJson(account);

		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(accountJsonString);
		out.flush();
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		BufferedReader reader = request.getReader();
		AccountDto accountDto = gson.fromJson(reader, AccountDto.class);
		accountService.saveAccount(accountDto);
		response.setStatus(HttpStatus.OK_200);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		BufferedReader reader = req.getReader();
		AccountDto accountDto = gson.fromJson(reader, AccountDto.class);
		accountService.updateAccount(accountDto);
		resp.setStatus(HttpStatus.OK_200);
	}
}
