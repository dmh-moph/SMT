package smt.auth.service;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smt.auth.model.SecurityUser;

@Component
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
	
	private static Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	@Autowired
	private SecUserRepository SecUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		
		SecurityUser user = SecUserRepository.findUserByUserName(userName);
		
		if(user == null){
			logger.debug("user is null");
			throw new UsernameNotFoundException("UserName "+userName+" not found");
		}
		logger.debug("size: " + user.getSecurityRoles().size());
		logger.debug("user: " + user.getId());
		logger.debug(user.toString());

		return user;
	}

}
