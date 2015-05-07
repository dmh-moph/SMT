package smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import smt.model.FileMeta;


public interface FileMetaRepo extends 
	JpaRepository<FileMeta, Long>, QueryDslPredicateExecutor<FileMeta> {

}
