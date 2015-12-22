package smt.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import smt.model.FileHistoryRecord;


public interface FileHistoryRecordRepo extends 
	JpaRepository<FileHistoryRecord, Long>, QueryDslPredicateExecutor<FileHistoryRecord> {

	@Query(value = ""
			+ "select j.dv_journal_type, count(r.id) "
			+ "from SMT_FILEHISTORYRECORD r, smt_filemeta m, smt_journal j "
			+ "where r.FILEMETA_ID = m.id "
			+ " 	and r.SEARCH_TIME BETWEEN to_date(?2,'dd/MM/yyyy') and to_date(?3,'dd/MM/yyyy')"
			+ "		and m.DOMAIN like ?1 "
			+ "		and m.DOMAINID = j.id "
			+ "group by j.DV_JOURNAL_TYPE",
			nativeQuery = true)
	List<Object[]> countStat(String domain, String startDate, String endDate);

}
