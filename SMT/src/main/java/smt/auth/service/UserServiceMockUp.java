package smt.auth.service;

import smt.auth.model.SecurityUser;
import smt.auth.model.User;

public class UserServiceMockUp implements UserService {

	@Override
	public User findUserByUserName(String userName) {
		
		return new SecurityUser(1L, userName, "xxxx", null);
	}

}
