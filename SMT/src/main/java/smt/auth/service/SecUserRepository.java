package smt.auth.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import smt.auth.model.SecurityUser;
import smt.auth.model.User;

public interface SecUserRepository extends JpaRepository<SecurityUser, Long> {

	@Query(""
			+ "SELECT user "
			+ "FROM SecurityUser user "
			+ "WHERE user.username like ?1 AND user.password like ?2")
	SecurityUser findByUserNameAndPassword(String userName, String password);

	@Query(""
			+ "SELECT user "
			+ "FROM SecurityUser user "
			+ "WHERE user.username like ?1")
	User findUserByUserName(String userName);

}
