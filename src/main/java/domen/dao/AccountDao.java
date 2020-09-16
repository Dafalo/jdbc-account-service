package domen.dao;

import model.Account;

public interface AccountDao {
	public static final String TABLE_NAME = "public.account";
	public static final String FIRST_NAME_COLUMN = "first_name";
	public static final String LAST_NAME_COLUMN = "last_name";

	static final String SQL_INSERT =
			"insert into " + TABLE_NAME + " (" + FIRST_NAME_COLUMN + ", " + LAST_NAME_COLUMN + ") values (?, ?)" +
					"on conflict (" + FIRST_NAME_COLUMN + ")" + "do update set " + LAST_NAME_COLUMN + " = excluded."
					+ LAST_NAME_COLUMN + ";";
	static final String SQL_UPDATE = "update " + TABLE_NAME + " set " + LAST_NAME_COLUMN + " = ?"
			+ " where " + FIRST_NAME_COLUMN + " = ?";
	static final String SQL_FIND_BY_NAME = "select * from " + TABLE_NAME + " where " + FIRST_NAME_COLUMN + " = $1";


	Account findByName(String name);

	void insert(Account account);

	void update(Account account);

}
