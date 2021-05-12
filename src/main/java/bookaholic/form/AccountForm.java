package bookaholic.form;

import bookaholic.entity.Account;
import bookaholic.model.AccountInfo;

public class AccountForm {
	private String accountUsername;
	private String accountPassword;
	private int accountId;
	private String accountName;
	private String accountPhone;
	private String accountEmail;
	private String accountAddress;
	private String accountRole;

	private boolean newAccount = false;

	private boolean valid;

	public AccountForm() {
		this.newAccount = true;
	}

	public AccountForm(AccountInfo accountInfo) {
		if (accountInfo != null) {
			this.accountUsername = accountInfo.getAccountUsername();
			this.accountPassword = accountInfo.getAccountPassword();
			this.accountId = accountInfo.getAccountId();
			this.accountName = accountInfo.getAccountName();
			this.accountPhone = accountInfo.getAccountPhone();
			this.accountEmail = accountInfo.getAccountEmail();
			this.accountAddress = accountInfo.getAccountAddress();
			this.accountRole = accountInfo.getAccountRole();
		}
	}

	public AccountForm(Account account) {
		this.accountUsername = account.getAccountUsername();
		this.accountPassword = account.getAccountPassword();
		this.accountId = account.getAccountId();
		this.accountName = account.getAccountName();
		this.accountPhone = account.getAccountPhone();
		this.accountEmail = account.getAccountEmail();
		this.accountAddress = account.getAccountAddress();
		this.accountRole = account.getAccountRole();
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

	public boolean isNewAccount() {
		return newAccount;
	}

	public void setNewAccount(boolean newAccount) {
		this.newAccount = newAccount;
	}

}
