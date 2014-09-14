package smt.auth.service;

import smt.auth.model.User;



public interface UserService {

	User findUserByUserName(String userName);

}
