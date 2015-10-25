package smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import smt.model.glb.DomainVariable;

public interface DomainVariableRepo extends JpaRepository<DomainVariable, Long> {

	@Query(""
			+ "SELECT dv "
			+ "FROM DomainVariable dv "
			+ "WHERE dv.domain = ?1 "
			+ "ORDER BY dv.sequence asc")
	public List<DomainVariable> findAllByDomainName(String domainName);

	@Query(""
			+ "SELECT dv "
			+ "FROM DomainVariable dv "
			+ "WHERE dv.domain = 'POSITION' "
			+ "	AND dv.code like ?1 "
			+ "ORDER BY dv.sequence asc")
	public List<DomainVariable> findAllPositionBeginWithCode(String code);
	
}
