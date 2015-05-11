package smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import smt.model.PsychoSocialReport;

public interface PsychoSocailReportRepo extends 
	JpaRepository<PsychoSocialReport, Long>, 
	QueryDslPredicateExecutor<PsychoSocialReport> {

}
