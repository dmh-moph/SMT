package smt.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import smt.auth.model.SecurityUser;
import smt.model.OrganizationNetwork;
import smt.model.glb.Amphur;
import smt.model.glb.DomainVariable;
import smt.model.glb.HealthZone;
import smt.model.glb.Province;
import smt.webUI.ResponseJSend;

public interface EntityService {

	public List<Province> findAllProvince();

	public List<Amphur> findAllAmphurByProvinceId(Long provinceId);

	public List<HealthZone> findAllHealthZone();

	public List<Province> findAllProvinceByHealthZoneId(Long zoneId);

	public List<DomainVariable> findAllDomainVariableByDomainName(
			String domainName);

	public DomainVariable findDomainVariableByDomainNameAndId(
			String domainName, Long id);

	public OrganizationNetwork findOrganizationNetworkById(Long id);

	public ResponseJSend<Long> saveOrganizationNetwork(JsonNode node, SecurityUser user);

}
