package dto;

public class AccountDto {
	private String firstName;
	private String lastName;

	public AccountDto() {
	}

	public AccountDto(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AccountDto that = (AccountDto) o;

		return firstName != null ? firstName.equals(that.firstName) : that.firstName == null;
	}

	@Override
	public int hashCode() {
		return firstName != null ? firstName.hashCode() : 0;
	}
}
