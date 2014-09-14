package smt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;

import smt.auth.service.CustomAuthenticationProvider;
import smt.auth.service.CustomUserDetailsService;
import smt.auth.service.UserService;
import smt.auth.service.UserServiceMockUp;

@Configuration
public class ApplicationConfig {

	@Bean 
	public UserService userService() {
		return new UserServiceMockUp();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		return new CustomAuthenticationProvider();
	}
}
