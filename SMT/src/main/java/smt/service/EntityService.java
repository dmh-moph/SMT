package smt.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import smt.auth.model.SecurityUser;
import smt.model.Behavior;
import smt.model.Journal;
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

	public ResponseJSend<Page<OrganizationNetwork>> findOrganizationNetworkByExample(
			JsonNode node, Integer pageNum);

	public ResponseJSend<Long> deleteOrganizationNetwork(Long id);

	public Behavior findBehaviorById(Long id);

	public ResponseJSend<Page<Behavior>> findBehaviorByExample(JsonNode node,
			Integer pageNum) throws JsonMappingException;

	public ResponseJSend<Long> saveBehavior(JsonNode node, SecurityUser user) throws JsonMappingException;

	public ResponseJSend<Long> deleteBehavior(Long id);

	public ResponseJSend<Page<Journal>> findJournalByExample(JsonNode node,
			Integer pageNum) throws JsonMappingException;

	public Journal findJournalById(Long id);

	public ResponseJSend<Long> saveJournal(JsonNode node, SecurityUser user) throws JsonMappingException;

	public ResponseJSend<Long> deleteJournal(Long id);

}
