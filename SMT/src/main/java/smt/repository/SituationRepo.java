package smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import smt.model.Situation;


public interface SituationRepo extends 
	JpaRepository<Situation, Long>, QueryDslPredicateExecutor<Situation> {

}
