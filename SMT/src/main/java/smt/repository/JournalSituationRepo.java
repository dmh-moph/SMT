package smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import smt.model.JournalSituation;

public interface JournalSituationRepo extends JpaRepository<JournalSituation, Long>,
	QueryDslPredicateExecutor<JournalSituation> {

}
