package smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import smt.model.OrganizationNetwork;

public interface OrganizationNetworkRepo extends 
	JpaRepository<OrganizationNetwork, Long>, 
	QueryDslPredicateExecutor<OrganizationNetwork> {

}
