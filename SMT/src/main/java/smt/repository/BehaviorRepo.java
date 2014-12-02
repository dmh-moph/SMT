package smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import smt.model.Behavior;


public interface BehaviorRepo extends 
	JpaRepository<Behavior, Long>, QueryDslPredicateExecutor<Behavior> {

}
