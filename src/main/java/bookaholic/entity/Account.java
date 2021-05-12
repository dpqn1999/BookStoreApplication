package bookaholic.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Accounts")
public class Account implements Serializable {
	private static final long serialVersionUID = -2054386655979281969L;
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_CUSTOMER = "CUSTOMER";

	@Id
	@Column(name = "accountUsername", length = 20, nullable = false)
	private String accountUsername;

	@Column(name = "accountPassword", length = 128, nullable = false)
	private String accountPassword;

	@Column(name = "accountId", nullable = false)
	private int accountId;

	@Column(name = "accountName", length = 50, nullable = false)
	private String accountName;

	@Column(name = "accountPhone", length = 10, nullable = false)
	private String accountPhone;

	@Column(name = "accountEmail", length = 50, nullable = false)
	private String accountEmail;

	@Column(name = "accountAddress", length = 50, nullable = false)
	private String accountAddress;

	@Column(name = "accountRole", length = 20, nullable = false)
	private String accountRole;

	public Account() {
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

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
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

	@Override
	public String toString() {
		return "[" + this.accountUsername + ", " + this.accountPassword + ", " + this.accountId + ", "
				+ this.accountName + "," + this.accountPhone + ", " + this.accountEmail + ", " + this.accountAddress
				+ ", " + this.accountRole + "]";
	}
}
