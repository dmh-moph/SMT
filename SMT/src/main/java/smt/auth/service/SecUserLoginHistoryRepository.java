package smt.auth.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import smt.auth.model.SecurityRole;
import smt.auth.model.SecurityUser;
import smt.auth.model.SecurityUserLoginHistory;

public interface SecUserLoginHistoryRepository extends JpaRepository<SecurityUserLoginHistory, Long>, QueryDslPredicateExecutor<SecurityUserLoginHistory> {

	@Query(""
			+ "SELECT count(*) "
			+ "FROM SecurityUserLoginHistory user ")
	public Integer countAllUser();

	
}
