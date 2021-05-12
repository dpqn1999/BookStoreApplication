package bookaholic.model;

import bookaholic.form.AccountForm;

public class AccountInfo {
	private String accountUsername;
	private String accountPassword;
	private int accountId;
	private String accountName;
	private String accountPhone;
	private String accountEmail;
	private String accountAddress;
	private String accountRole;

	private boolean valid;

	public AccountInfo() {
	}

	public AccountInfo(AccountForm accountForm) {
		this.accountUsername = accountForm.getAccountUsername();
		this.accountPassword = accountForm.getAccountPassword();
		this.accountId = accountForm.getAccountId();
		this.accountName = accountForm.getAccountName();
		this.accountPhone = accountForm.getAccountPhone();
		this.accountEmail = accountForm.getAccountEmail();
		this.accountAddress = accountForm.getAccountAddress();
		this.accountRole = accountForm.getAccountRole();
		this.valid = accountForm.isValid();
	}

	public AccountInfo(String accountUsername, String accountPassword, int accountId, String accountName,
			String accountPhone, String accountEmail, String accountAddress, String accountRole) {
		this.accountUsername = accountUsername;
		this.accountPassword = accountPassword;
		this.accountId = accountId;
		this.accountName = accountName;
		this.accountPhone = accountPhone;
		this.accountEmail = accountEmail;
		this.accountAddress = accountAddress;
		this.accountRole = accountRole;
	}

	public AccountInfo(int accountId, String accountUsername, String accountName, String accountPhone, String accountEmail,
			String accountAddress) {
		this.accountId = accountId;
		this.accountUsername = accountUsername;
		this.accountName = accountName;
		this.accountPhone = accountPhone;
		this.accountEmail = accountEmail;
		this.accountAddress = accountAddress;
	}

	public String getAccountUsername() {
		return accountUsername;
	}

	public void setAccountUsername(String accountUsername) {
		this.accountUsername = accountUsername;
	}

	public String getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountPhone() {
		return accountPhone;
	}

	public void setAccountPhone(String accountPhone) {
		this.accountPhone = accountPhone;
	}

	public String getAccountEmail() {
		return accountEmail;
	}

	public void setAccountEmail(String accountEmail) {
		this.accountEmail = accountEmail;
	}

	public String getAccountAddress() {
		return accountAddress;
	}

	public void setAccountAddress(String accountAddress) {
		this.accountAddress = accountAddress;
	}

	public String getAccountRole() {
		return accountRole;
	}

	public void setAccountRole(String accountRole) {
		this.accountRole = accountRole;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
