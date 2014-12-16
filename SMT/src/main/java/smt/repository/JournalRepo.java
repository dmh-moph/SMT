package smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import smt.model.Journal;

public interface JournalRepo extends JpaRepository<Journal, Long>,
		QueryDslPredicateExecutor<Journal> {

}
