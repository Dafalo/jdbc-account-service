package services;

import domain.dao.AccountDao;
import dto.AccountDto;
import mapper.AccountMapper;
import model.Account;

public class AccountService {

	private final AccountDao accountDao;
	private final AccountMapper accountMapper;

	public AccountService(AccountDao accountDao, AccountMapper accountMapper) {
		this.accountDao = accountDao;
		this.accountMapper = accountMapper;
	}

	public void updateAccount(AccountDto accountDto) {
		Account account = accountMapper.toEntity(accountDto);
		accountDao.update(account);
	}

	public AccountDto getAccount(String firstName) {
		Account account = accountDao.findByName(firstName);
		return accountMapper.toDto(account);
	}

	public void saveAccount(AccountDto accountDto) {
		Account account = accountMapper.toEntity(accountDto);
		accountDao.insert(account);
	}
}
