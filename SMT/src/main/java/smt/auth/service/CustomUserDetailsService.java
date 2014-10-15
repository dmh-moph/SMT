package smt.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import smt.auth.model.SecurityUser;
import smt.auth.model.User;

public class CustomUserDetailsService implements UserDetailsService {
	
	public static Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	@Autowired
	private SecUserRepository secUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		
		
		
		User user = secUserRepository.findUserByUserName(userName);
		if(user == null){
			logger.debug("user is null");
			throw new UsernameNotFoundException("UserName "+userName+" not found");
		}
		logger.debug("user: " + user.getId());
		
		
		return new SecurityUser(user);
	}

}
