package bookaholic.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import bookaholic.dao.AccountDAO;
import bookaholic.entity.Account;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private AccountDAO accountDAO;

	@Override
	public UserDetails loadUserByUsername(String accountUsername) throws UsernameNotFoundException {
		Account account = accountDAO.findAccount(accountUsername);
		if (account == null) {
			throw new UsernameNotFoundException("User " //
					+ accountUsername + " was not found in the database");
		}
		String accountRole = account.getAccountRole();
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		GrantedAuthority authority = new SimpleGrantedAuthority(accountRole);
		grantList.add(authority);
		UserDetails userDetails = (UserDetails) new User(account.getAccountUsername(), //
				account.getAccountPassword(), grantList);

		return userDetails;
	}

	public static Connection getConnection(String dbURL, String userName, String password) {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, userName, password);
			System.out.println("Connect successfully!");
		} catch (Exception ex) {
			System.out.println("Connect failure!");
			ex.printStackTrace();
		}
		return conn;
	}
}
