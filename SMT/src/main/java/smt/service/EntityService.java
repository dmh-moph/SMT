package smt.service;

import java.util.List;

import smt.model.glb.Amphur;
import smt.model.glb.DomainVariable;
import smt.model.glb.HealthZone;
import smt.model.glb.Province;

public interface EntityService {

	public List<Province> findAllProvince();

	public List<Amphur> findAllAmphurByProvinceId(Long provinceId);

	public List<HealthZone> findAllHealthZone();

	public List<Province> findAllProvinceByHealthZoneId(Long zoneId);

	public List<DomainVariable> findAllDomainVariableByDomainName(
			String domainName);

}
