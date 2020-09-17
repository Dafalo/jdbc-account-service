package mapper;

import dto.AccountDto;
import model.Account;

import java.util.Objects;

public class AccountMapper {

	public Account toEntity(AccountDto accountDto) {
		if (!Objects.isNull(accountDto)) {
			return mapAccount(accountDto);
		} else throw new RuntimeException("Input data is null");
	}

	public AccountDto toDto(Account account) {
		if (!Objects.isNull(account)) {
			return new AccountDto(account.getFirstName(), account.getLastName());
		} else throw new RuntimeException("Input data is null");

	}

	private Account mapAccount(AccountDto accountDto) {
		Account account = new Account();
		account.setFirstName(accountDto.getFirstName());
		account.setLastName(accountDto.getLastName());
		return account;
	}
}
