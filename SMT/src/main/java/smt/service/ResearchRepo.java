package smt.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import smt.model.Research;

public interface ResearchRepo extends JpaRepository<Research, Long>,
		QueryDslPredicateExecutor<Research> {

}
