package smt.auth.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.context.support.WebApplicationContextUtils;

import smt.auth.model.SecurityUser;
import smt.auth.model.SecurityUserLoginHistory;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

private static Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
	
	@Autowired
	private SecUserLoginHistoryRepository secUserLoginHistoryRepository;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
			throws IOException, ServletException {
		SecurityUser user = (SecurityUser) auth.getPrincipal();
		logger.debug("successful login from: " + user.getUsername());
		
		logger.debug("saving user login record");
		// now save this user
		SecurityUserLoginHistory loginHistory = new SecurityUserLoginHistory(user, request);
		secUserLoginHistoryRepository.save(loginHistory);
		
		
		//now redirect to default login
		response.sendRedirect("/smt/");

	}

}
