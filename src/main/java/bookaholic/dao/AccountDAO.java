package bookaholic.dao;

import javax.persistence.NoResultException;

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import bookaholic.entity.Account;
import bookaholic.entity.Book;
import bookaholic.form.AccountForm;
import bookaholic.model.AccountInfo;
import bookaholic.pagination.PaginationResult;

@Transactional
@Repository
public class AccountDAO {

	@Autowired
	private SessionFactory sessionFactory;

	
	public Account findAccount(String accountUsername) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.find(Account.class, accountUsername);
	}
	
	
	public Account findAccountByPhone(String accountPhone) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.find(Account.class, accountPhone);
	}

	public AccountInfo findAccountInfo(String accountUsername) {
		Account account = this.findAccount(accountUsername);
		if (account == null) {
			return null;
		}
		return new AccountInfo(account.getAccountUsername(), account.getAccountPassword(), account.getAccountId(),
				account.getAccountName(), account.getAccountPhone(), account.getAccountEmail(),
				account.getAccountAddress(), account.getAccountRole());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void saveAccount(AccountForm accountForm) {
		Session session = this.sessionFactory.getCurrentSession();
		String accountUsername = accountForm.getAccountUsername();
		Account account = null;
		boolean isNew = false;
		if (accountUsername != null) {
			account = this.findAccount(accountUsername);
		}
		if (account == null) {
			isNew = true;
			account = new Account();
		}
		account.setAccountUsername(accountForm.getAccountUsername());
		account.setAccountPassword(accountForm.getAccountPassword());
		account.setAccountId(accountForm.getAccountId());
		account.setAccountName(accountForm.getAccountName());
		account.setAccountPhone(accountForm.getAccountPhone());
		account.setAccountEmail(accountForm.getAccountEmail());
		account.setAccountAddress(accountForm.getAccountAddress());
		account.setAccountRole(accountForm.getAccountRole());
		if (isNew) {
			session.persist(account);
		}
		session.flush();
	}

	public PaginationResult<AccountInfo> queryAccounts(int page, int maxResult, int maxNavigationPage,
			String likeName) {
		String sql = "SELECT NEW " + AccountInfo.class.getName() //
				+ "(a.accountUsername, a.accountPassword, a.accountId, a.accountName, a.accountPhone, a.accountEmail, a.accountAddress, a.accountRole)"
				+ " from " + Account.class.getName() + " a ";
		if (likeName != null && likeName.length() > 0) {
			sql += "WHERE LOWER(a.accountName) LIKE :likeName ";
		}
		sql += " ORDER BY a.accountName";
		Session session = this.sessionFactory.getCurrentSession();
		Query<AccountInfo> query = session.createQuery(sql, AccountInfo.class);
		if (likeName != null && likeName.length() > 0) {
			query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
		}
		return new PaginationResult<AccountInfo>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<AccountInfo> queryAccounts(int page, int maxResult, int maxNavigationPage) {
		return queryAccounts(page, maxResult, maxNavigationPage, null);
	}

	// @page = 1, 2, ...
	public PaginationResult<AccountInfo> listAccountInfo(int page, int maxResult, int maxNavigationPage) {
		String sql = "SELECT NEW " + AccountInfo.class.getName()//
				+ "(a.accountId, a.accountUsername, a.accountName, a.accountPhone, a.accountEmail, a.accountAddress)"
				+ " FROM " + Account.class.getName() + " a "//
				+ " WHERE a.accountRole = 'ROLE_CUSTOMER'" //
				+ " ORDER BY a.accountId ";
		Session session = this.sessionFactory.getCurrentSession();
		Query<AccountInfo> query = session.createQuery(sql, AccountInfo.class);
		return new PaginationResult<AccountInfo>(query, page, maxResult, maxNavigationPage);
	}

	private int getMaxAccountId() {
		String sql = "Select max(a.accountId) from " + Account.class.getName() + " a ";
		Session session = this.sessionFactory.getCurrentSession();
		Query<Integer> query = session.createQuery(sql, Integer.class);
		Integer value = (Integer) query.getSingleResult();
		if (value == null) {
			return 0;
		}
		return value;
	}

	public boolean createAccount(AccountForm accountForm) {
		boolean result = false;
		Session session = this.sessionFactory.getCurrentSession();
		String accountUsername = accountForm.getAccountUsername();
		Account account = null;
		boolean isNew = false;
		if (accountUsername != null) {
			account = this.findAccount(accountUsername);
		}
		if (account == null) {
			isNew = true;
		}
		if (isNew) {
			account = new Account();
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedPassword = passwordEncoder.encode(accountForm.getAccountPassword());
			account.setAccountUsername(accountForm.getAccountUsername());
			account.setAccountPassword(encodedPassword);
			account.setAccountId(getMaxAccountId() + 1);
			account.setAccountName(accountForm.getAccountName());
			account.setAccountPhone(accountForm.getAccountPhone());
			account.setAccountEmail(accountForm.getAccountEmail());
			account.setAccountAddress(accountForm.getAccountAddress());
			account.setAccountRole("ROLE_CUSTOMER");
			session.save(account);
			result = true;
		}
		session.flush();
		return result;
	}

	public boolean changePassword(String accountUsername, String newPassword) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(newPassword);
		String sql = "UPDATE " + Account.class.getName() + " SET accountPassword = '" + encodedPassword
				+ "' WHERE (accountUsername = '" + accountUsername + "')";
		Session session = this.sessionFactory.getCurrentSession();
		Query<Account> query = session.createQuery(sql);
		int result = query.executeUpdate();
		if (result > 0) {
			return true;
		}
		return false;
	}

	public String resetAccountPassword(String email) {
		Account account = findAccountByEmail(email);
		String randomPassword = RandomStringUtils.randomAlphanumeric(10);
		changePassword(account.getAccountUsername(), randomPassword);
		return randomPassword;
	}

	private Account findAccountByEmail(String email) {
		try {
			String sql = "SELECT a FROM " + Account.class.getName() + " a WHERE a.accountEmail =:email";
			Session session = this.sessionFactory.getCurrentSession();
			Query<Account> query = session.createQuery(sql, Account.class);
			query.setParameter("email", email);
			return (Account) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
