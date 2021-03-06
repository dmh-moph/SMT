package smt.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import smt.auth.model.SecurityUser;
import smt.model.Behavior;
import smt.model.Journal;
import smt.model.JournalSituation;
import smt.model.OrganizationNetwork;
import smt.model.PsychoSocialReport;
import smt.model.Research;
import smt.model.ResearchSituation;
import smt.model.Situation;
import smt.model.glb.Amphur;
import smt.model.glb.DomainVariable;
import smt.model.glb.HealthZone;
import smt.model.glb.Province;
import smt.webUI.DomainCountTuple;
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
			Integer pageNum, SecurityUser user) throws JsonMappingException;

	public Journal findJournalById(Long id);

	public ResponseJSend<Long> saveJournal(JsonNode node, SecurityUser user) throws JsonMappingException;

	public ResponseJSend<Long> deleteJournal(Long id);
	
	public ResponseJSend<Page<Research>> findResearchByExample(JsonNode node,
			Integer pageNum) throws JsonMappingException;

	public Research findResearchById(Long id);

	public ResponseJSend<Long> saveResearch(JsonNode node, SecurityUser user) throws JsonMappingException;

	public ResponseJSend<Long> deleteResearch(Long id);

	public ResponseJSend<Page<Situation>> findSituationByExample(JsonNode node,
			Integer pageNum) throws JsonMappingException;

	public Situation findSituationById(Long id);

	public ResponseJSend<Long> saveSituation(JsonNode node, SecurityUser user) throws JsonMappingException;

	public ResponseJSend<Long> deleteSituation(Long id);

	public List<Situation> findAllSituation();

	public ResponseJSend<Page<JournalSituation>> findJournalSituationByExample(
			JsonNode node, Integer pageNum);

	public JournalSituation findJournalSituationById(Long id);

	public ResponseJSend<Long> saveJournalSituation(JsonNode node,
			SecurityUser user);

	public ResponseJSend<Long> deleteJournalSituation(Long id);
	
	
	public ResponseJSend<Page<ResearchSituation>> findResearchSituationByExample(
			JsonNode node, Integer pageNum);

	public ResearchSituation findResearchSituationById(Long id);

	public ResponseJSend<Long> saveResearchSituation(JsonNode node,
			SecurityUser user);

	public ResponseJSend<Long> deleteResearchSituation(Long id);

	public ResponseJSend<Page<PsychoSocialReport>> findPsychoSocialReportByExample(
			JsonNode node, Integer pageNum) throws JsonMappingException;

	public PsychoSocialReport findPsychoSocialReportById(Long id);

	public ResponseJSend<Long> savePsychoSocialReport(JsonNode node,
			SecurityUser user) throws JsonMappingException;

	public ResponseJSend<Long> deletePsychoSocialReport(Long id);

	public Iterable<OrganizationNetwork> findAllOrganizationByProvinceId(
			Long provinceId);

	public Iterable<PsychoSocialReport> findPsychoSocialReportByExample(
			PsychoSocialReport exampleReport);

	public List<DomainVariable> findAllDomainVariablePostionByOccupationId(Long id);

	public Iterable<DomainCountTuple> findHistoryCountBetween(String domain, String start, String end);

}
