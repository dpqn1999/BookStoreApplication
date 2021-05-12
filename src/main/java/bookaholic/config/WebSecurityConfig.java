package bookaholic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import bookaholic.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/", "/login", "/logout").permitAll();
		http.authorizeRequests().antMatchers("/accountInfo", "/order")
				.access("hasAnyRole('ROLE_CUSTOMER', 'ROLE_ADMIN')");
		http.authorizeRequests().antMatchers("/admin/bookList", "/admin/book", "/admin/authorList", "/admin/author",
				"/admin/publisherList", "/admin/publisher", "/admin/customerList", "/admin/orderList", "/admin/order")
				.access("hasRole('ROLE_ADMIN')");
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
		
		http.authorizeRequests().and().formLogin()//
				.loginProcessingUrl("/j_spring_security_check")//
				.loginPage("/login")//
				.defaultSuccessUrl("/")//
				.failureUrl("/login?error=true")//
				.usernameParameter("username")//
				.passwordParameter("password")//
				.and().logout().logoutUrl("/logout").logoutSuccessUrl("/");
		
		http.authorizeRequests().and().formLogin()//
				.loginProcessingUrl("/j_spring_security_check")//
				.loginPage("/admin")//
				.defaultSuccessUrl("/")//
				.failureUrl("/login?error=true")//
				.usernameParameter("username")//
				.passwordParameter("password")//
				.and().logout().logoutUrl("/logout").logoutSuccessUrl("/");
	}
}
