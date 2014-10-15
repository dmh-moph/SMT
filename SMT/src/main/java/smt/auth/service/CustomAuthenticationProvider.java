package smt.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import smt.auth.model.SecurityUser;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private SecUserRepository secUserRepository;
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String userName = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		SecurityUser secUser = secUserRepository.findByUserNameAndPassword(userName, password);
		
		if(secUser != null) {
			List<GrantedAuthority> grantedAuths = new ArrayList<>();
			grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
				
			Authentication auth = new UsernamePasswordAuthenticationToken(secUser, password, grantedAuths );
			
			return auth;
		
		} 

		throw new BadCredentialsException("UserName and Password cannot be found!");

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
