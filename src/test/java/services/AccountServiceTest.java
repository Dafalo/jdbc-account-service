package services;

import dto.AccountDto;
import mapper.AccountMapper;
import model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {
	private AccountService accountService = mock(AccountService.class);
	private AccountMapper accountMapper = mock(AccountMapper.class);

	@Test
	public void verifyUpdateAccount() {
		Account account = getAccount();
		when(accountMapper.toEntity(anyObject())).thenReturn(account);
		AccountDto accountDto = getAccountDto();
		accountService.updateAccount(accountDto);
		verify(accountService).updateAccount(accountDto);
	}

	@Test
	public void callSaveAccount() {
		Account account = getAccount();
		when(accountMapper.toEntity(anyObject())).thenReturn(account);
		AccountDto accountDto = getAccountDto();
		accountService.saveAccount(accountDto);
		verify(accountService).saveAccount(accountDto);
	}

	@Test
	public void verifyGetAccount() {
		AccountDto accountDto = getAccountDto();
		when(accountMapper.toDto(anyObject())).thenReturn(accountDto);
		accountService.getAccount(accountDto.getFirstName());
		verify(accountService).getAccount(accountDto.getFirstName());
	}

	private Account getAccount() {
		Account account = new Account();
		account.setFirstName("First");
		account.setLastName("Last");
		return account;
	}

	private AccountDto getAccountDto() {
		return new AccountDto("First", "Last");
	}
}
