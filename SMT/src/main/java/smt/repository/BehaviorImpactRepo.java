package smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import smt.model.BehaviorImpact;

public interface BehaviorImpactRepo extends QueryDslPredicateExecutor<BehaviorImpact>,
		JpaRepository<BehaviorImpact, Long> {

}
