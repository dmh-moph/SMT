package smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import smt.model.glb.Amphur;
import smt.model.glb.HealthZone;
import smt.model.glb.Province;

public interface ProvinceRepo extends JpaRepository<Province, Long> {

	@Query(""
			+ "SELECT a "
			+ "FROM Amphur a "
			+ "		INNER JOIN a.province p "
			+ "WHERE p.id = ?1 and a.code < 50 "
			+ "ORDER BY a.code asc ")
	List<Amphur> findAllAmphurByProvinceId(Long provinceId);


	@Query(""
			+ "SELECT p "
			+ "FROM Province p "
			+ "		INNER JOIN p.zone z "
			+ "WHERE z.id = ?1 "
			+ "ORDER BY p.code asc ")
	List<Province> findAllProvinceByHealthZone(Long zoneId);
	
	@Query(""
			+ "SELECT z "
			+ "FROM HealthZone z "
			+ "ORDER BY z.id asc")
	List<HealthZone> findAllHealthZone();
	
}
