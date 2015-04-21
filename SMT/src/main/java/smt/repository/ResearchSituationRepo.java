package smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import smt.model.JournalSituation;
import smt.model.ResearchSituation;

public interface ResearchSituationRepo extends JpaRepository<ResearchSituation, Long>,
	QueryDslPredicateExecutor<ResearchSituation> {

}
