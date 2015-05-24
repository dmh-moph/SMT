package smt.auth.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import smt.auth.model.SecurityRole;
import smt.auth.model.SecurityUser;
import smt.auth.model.User;

public interface SecUserRepository extends JpaRepository<SecurityUser, Long>, QueryDslPredicateExecutor<SecurityUser> {

	@Query(""
			+ "SELECT user "
			+ "FROM SecurityUser user "
			+ "WHERE user.username like ?1 AND user.password like ?2")
	public SecurityUser findByUserNameAndPassword(String userName, String password);

	@Query(""
			+ "SELECT user "
			+ "FROM SecurityUser user "
			+ "WHERE user.username like ?1")
	public SecurityUser findUserByUserName(String userName);

	@Query(""
			+ "SELECT role "
			+ "FROM SecurityRole role "
			+ "WHERE role.name like 'USER'")
	public SecurityRole findUserRole();
	

}
