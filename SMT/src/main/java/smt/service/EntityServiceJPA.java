package smt.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.crsh.console.jline.internal.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.expr.BooleanExpression;

import smt.auth.model.SecurityUser;
import smt.auth.service.SecUserRepository;
import smt.model.Behavior;
import smt.model.BehaviorImpact;
import smt.model.FileHistoryRecord;
import smt.model.Journal;
import smt.model.JournalSituation;
import smt.model.OrganizationNetwork;
import smt.model.OrganizationPerson;
import smt.model.PsychoSocialReport;
import smt.model.QBehavior;
import smt.model.QFileHistoryRecord;
import smt.model.QJournal;
import smt.model.QJournalSituation;
import smt.model.QOrganizationNetwork;
import smt.model.QPsychoSocialReport;
import smt.model.QResearch;
import smt.model.QResearchSituation;
import smt.model.QSituation;
import smt.model.Research;
import smt.model.ResearchSituation;
import smt.model.Situation;
import smt.model.glb.Amphur;
import smt.model.glb.DomainVariable;
import smt.model.glb.HealthZone;
import smt.model.glb.JournalType;
import smt.model.glb.NetworkType;
import smt.model.glb.OrgType;
import smt.model.glb.PersonType;
import smt.model.glb.Province;
import smt.model.glb.SchoolType;
import smt.model.glb.SituationType;
import smt.repository.AmphurRepo;
import smt.repository.BehaviorImpactRepo;
import smt.repository.BehaviorRepo;
import smt.repository.DomainVariableRepo;
import smt.repository.FileHistoryRecordRepo;
import smt.repository.HealthZoneRepo;
import smt.repository.JournalRepo;
import smt.repository.JournalSituationRepo;
import smt.repository.JournalTypeRepo;
import smt.repository.NetworkTypeRepo;
import smt.repository.OrgTypeRepo;
import smt.repository.OrganizationNetworkRepo;
import smt.repository.OrganizationPersonRepo;
import smt.repository.PersonTypeRepo;
import smt.repository.ProvinceRepo;
import smt.repository.PsychoSocailReportRepo;
import smt.repository.ResearchSituationRepo;
import smt.repository.SchoolTypeRepo;
import smt.repository.SituationRepo;
import smt.repository.SituationTypeRepo;
import smt.webUI.DefaultProperty;
import smt.webUI.DomainCountTuple;
import smt.webUI.ResponseJSend;
import smt.webUI.ResponseStatus;

public class EntityServiceJPA implements EntityService {

	public static Logger logger = LoggerFactory.getLogger(EntityServiceJPA.class);
	
	@Autowired
	ProvinceRepo provinceRepo;
	
	@Autowired
	DomainVariableRepo domainVariableRepo;
	
	@Autowired
	OrganizationNetworkRepo organizationNetworkRepo;
	
	@Autowired
	BehaviorRepo behaviorRepo; 

	@Autowired
	BehaviorImpactRepo behaviorImpactRepo;
	
	@Autowired
	OrganizationPersonRepo organizationPersonRepo;
	
	@Autowired
	HealthZoneRepo healthZoneRepo;

	@Autowired
	AmphurRepo amphurRepo;
	
	@Autowired
	NetworkTypeRepo networkTypeRepo;
	
	@Autowired
	SchoolTypeRepo schoolTypeRepo;
	
	@Autowired
	PersonTypeRepo personTypeRepo;
	
	@Autowired
	OrgTypeRepo orgTypeRepo;
	
	@Autowired
	SecUserRepository secUserRepo;
	
	@Autowired
	JournalRepo journalRepo;
	
	@Autowired
	ResearchRepo researchRepo;

	@Autowired
	SituationRepo situationRepo;

	@Autowired
	SituationTypeRepo situationTypeRepo;
	
	@Autowired
	JournalTypeRepo journalTypeRepo;
	
	@Autowired
	JournalSituationRepo journalSituationRepo;
	
	@Autowired
	ResearchSituationRepo researchSituationRepo;
	
	@Autowired
	PsychoSocailReportRepo psychoSocailReportRepo;
	
	@Autowired
	FileHistoryRecordRepo fileHistoryRecordRepo;
	
	SimpleDateFormat ddmmyyyyFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
	
	@Override
	public List<Province> findAllProvince() {
		return provinceRepo.findAll();
	}
	
	@Override
	public List<Amphur> findAllAmphurByProvinceId(Long provinceId) {
		return provinceRepo.findAllAmphurByProvinceId(provinceId);
	}
	
	@Override
	public Iterable<OrganizationNetwork> findAllOrganizationByProvinceId(
			Long provinceId) {
		QOrganizationNetwork q = QOrganizationNetwork.organizationNetwork;
		return organizationNetworkRepo.findAll(q.province.id.eq(provinceId));
	}

	@Override
	public List<HealthZone> findAllHealthZone() {
		return provinceRepo.findAllHealthZone();
	}

	@Override
	public List<Province> findAllProvinceByHealthZoneId(Long zoneId) {
		return provinceRepo.findAllProvinceByHealthZone(zoneId);
	}

	@Override
	public List<DomainVariable> findAllDomainVariableByDomainName(
			String domainName) {
		
		return domainVariableRepo.findAllByDomainName(domainName);
	}
	
	

	@Override
	public List<DomainVariable> findAllDomainVariablePostionByOccupationId(Long id) {
		DomainVariable dv = domainVariableRepo.findOne(id);
		if(dv.getDomain().equals("OCCUPATION")) {
			
			String code = dv.getCode();
			
			return domainVariableRepo.findAllPositionBeginWithCode(code+"%");
			
		} 
		return null;
	}

	@Override
	public DomainVariable findDomainVariableByDomainNameAndId(
			String domainName, Long id) {
		return domainVariableRepo.findOne(id);
	}

	@Override
	public OrganizationNetwork findOrganizationNetworkById(Long id) {
		OrganizationNetwork org = organizationNetworkRepo.findOne(id);
		org.getFiles().size();
		org.getMedicalStaffs().size();
		return org;
	}

	@Override
	public ResponseJSend<Long> saveOrganizationNetwork(JsonNode node, SecurityUser user) {
		OrganizationNetwork model;		
		if(node.get("id") != null) {
			// this is update
			model = organizationNetworkRepo.findOne(node.get("id").asLong());
			
			model.setLastUpdateBy(user);
			model.setLastUpdateDate(new Date());
		} else {
			// this is new
			model = new OrganizationNetwork();
			model.setCreateBy(user);
			model.setCreateDate(new Date());
			model.setLastUpdateBy(user);
			model.setLastUpdateDate(new Date());
		}
		
		// now copy properties to model
		model.setAddress(node.get("address").asText());
		model.setOrgName(node.get("orgName").asText());
		
		model.setOrgCode1(node.path("orgCode1").asText());
		model.setOrgCode2(node.path("orgCode2").asText());
		model.setContactPerson(node.path("contactPerson").asText());
		model.setTelephone(node.path("telephone").asText());
		model.setEmail(node.path("email").asText());
		model.setWebsite(node.path("website").asText());
		model.setTeenFriendly(node.path("teenFriendly").asBoolean());
		model.setSchoolName(node.path("schoolName").asText());
		

		// now the SLT
		if(node.get("zone") != null) {
			Long zoneId = node.get("zone").get("id").asLong();
			HealthZone zone = healthZoneRepo.findOne(zoneId);
			
			model.setZone(zone);
		}
		
		if(node.get("province") != null) {
			Long provinceId = node.get("province").get("id").asLong();
			Province province = provinceRepo.findOne(provinceId);
			
			model.setProvince(province);
		}
		
		if(node.get("amphur") != null) {
			Long amphurId = node.get("amphur").get("id").asLong();
			Amphur amphur = amphurRepo.findOne(amphurId);
			
			model.setAmphur(amphur);
		}
		
		if(node.get("networkType") != null) {
			Long networkTypeId = node.get("networkType").get("id").asLong();
			NetworkType networkType = networkTypeRepo.findOne(networkTypeId);
			
			model.setNetworkType(networkType);
		}
		
		if(node.get("schoolType") != null) {
			Long schoolTypeId = node.get("schoolType").get("id").asLong();
			SchoolType schoolType = schoolTypeRepo.findOne(schoolTypeId);
			
			model.setSchoolType(schoolType);
		}
		
		if(node.get("orgType") != null) {
			Long orgTypeId = node.get("orgType").get("id").asLong();
			OrgType orgType = orgTypeRepo.findOne(orgTypeId);
			
			model.setOrgType(orgType);
		}
		
		
		
		organizationNetworkRepo.save(model);
		
		// now we will deal with medicalStaffs
		List<OrganizationPerson> staffs = new ArrayList<OrganizationPerson>();
		
		
		
		for(JsonNode staffNode : node.get("medicalStaffs") ) {
			
			logger.debug("staffNode: " +staffNode.toString());
			OrganizationPerson staff;
			if(staffNode.get("id") != null) {
				staff = organizationPersonRepo.findOne(staffNode.get("id").asLong());
				
			} else { 
				staff = new OrganizationPerson();
				staff.setCreateBy(user);
				staff.setCreateDate(new Date());
			}
			
			staff.setLastUpdateBy(user);
			staff.setLastUpdateDate(new Date());
			
			staff.setName(staffNode.get("name").asText());
			
			if(staffNode.get("type") != null) {
				Long typeId = staffNode.get("type").get("id").asLong();
				PersonType type = personTypeRepo.findOne(typeId);
				staff.setType(type);
			}
			
			
			staff.setOrganizationNetwork(model);
			staffs.add(staff);
		}
		
		List<OrganizationPerson> staffsNetwork = model.getMedicalStaffs();
		if(staffsNetwork == null) {
			model.setMedicalStaffs(new ArrayList<OrganizationPerson>() ) ;
			staffsNetwork = model.getMedicalStaffs();
		}
		staffsNetwork.removeAll(staffs);
		
		organizationPersonRepo.delete(staffsNetwork);
		
		staffsNetwork.clear();
		staffsNetwork.addAll(staffs);
		
		organizationPersonRepo.save(staffs);
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.status = ResponseStatus.SUCCESS;
		response.data = model.getId(); 
		return response;
	}

	@Override
	public ResponseJSend<Page<OrganizationNetwork>> findOrganizationNetworkByExample(
			JsonNode node, Integer pageNum) {
		
		PageRequest pageRequest =
	            new PageRequest(pageNum -1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, new Sort(
	            		new Order(Direction.ASC, "orgName"),new Order(Direction.ASC, "id") 
	            		) );

		
		QOrganizationNetwork q = QOrganizationNetwork.organizationNetwork;
		
		logger.debug("node: " + node.toString());
		
		String orgNameStr = "%";
		if(node.get("orgName") != null && node.get("orgName").asText().length() > 0) {
			orgNameStr += node.get("orgName").asText() + "%";
		}
		BooleanExpression p = q.orgName.like(orgNameStr);
		logger.debug("orgName: " + orgNameStr);
		
		
		if(node.get("orgType") != null && node.get("orgType").get("id") !=null) {
			Long orgTypeId = node.get("orgType").get("id").asLong();
			p = p.and(q.orgType.id.eq(orgTypeId));
		}
		
		if(node.get("networkType") != null && node.get("networkType").get("id") != null) {
			Long networkTypeId = node.get("networkType").get("id").asLong();
			p = p.and(q.networkType.id.eq(networkTypeId));
		}
 		
		if(node.get("zone") != null && node.get("zone").get("id") != null) {
			Long zoneId = node.get("zone").get("id").asLong();
			p = p.and(q.zone.id.eq(zoneId));
		}
		
		if(node.get("province") != null && node.get("province").get("id") != null) {
			Long provinceId = node.get("province").get("id").asLong();
			p = p.and(q.province.id.eq(provinceId));
		}
		
		if(node.get("schoolName") != null && node.get("schoolName").asText().length() > 0) {
			p = p.and(q.schoolName.contains(node.get("schoolName").asText()));
			logger.debug("schoolName: " + node.get("schoolName").asText());
		}
		
		Page<OrganizationNetwork> orgs  = organizationNetworkRepo.findAll(p, pageRequest);
		
		for(OrganizationNetwork org : orgs) {
			org.getMedicalStaffs().size();
		}
		
		ResponseJSend<Page<OrganizationNetwork>> response = new ResponseJSend<Page<OrganizationNetwork>>();
		response.data=orgs;
		response.status=ResponseStatus.SUCCESS;
		return response;
	}

	@Override
	public ResponseJSend<Long> deleteOrganizationNetwork(Long id) {
		
		OrganizationNetwork org = organizationNetworkRepo.findOne(id);
		
		if(org != null) {
			organizationNetworkRepo.delete(org);
		}
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.data = id;
		response.status = ResponseStatus.SUCCESS;
		return response;
	}

	@Override
	public Behavior findBehaviorById(Long id) {
		Behavior behavior =behaviorRepo.findOne(id);
		behavior.getZone().getId();
		behavior.getProvince().getId();
		behavior.getFiles().size();
		if(behavior.getSituationType() != null) {
			behavior.getSituationType().getId();
		}
		behavior.getImpacts().size();
		behavior.getTargetEducationLevel().getId();
		return behavior; 
	}

	@Override
	public ResponseJSend<Page<Behavior>> findBehaviorByExample(JsonNode node,
			Integer pageNum) throws JsonMappingException {
		logger.debug("findBehaviorByExample");
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		Behavior webModel;
		
		try {
			webModel = mapper.treeToValue(node, Behavior.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonMappingException(e.getMessage() + "\n  JSON: " + node.toString());
		}
		
		QBehavior q = QBehavior.behavior;
		
		BooleanBuilder p = new BooleanBuilder();
		
		
		
		p = p.and(q.type.eq(webModel.getType()));
		
		if(webModel.getDescription() != null && webModel.getDescription().trim().length() > 0) {
			logger.debug("Searching description: %"+webModel.getDescription().trim()+"%");
			p = p.and(q.description.like("%"+webModel.getDescription().trim()+"%"));
		} 
		
		if(webModel.getYear() != null) {
			logger.debug("Searching year : "+ webModel.getYear());
			p = p.and(q.year.eq(webModel.getYear()));
		}
		
		if(webModel.getProvince() != null) {
			logger.debug("Searching Province : "+ webModel.getProvince().getId());
			p = p.and(q.province.id.eq(webModel.getProvince().getId()));
		}
		
		if(webModel.getZone() != null) {
			logger.debug("Searching Zone : "+ webModel.getZone().getId());
			p = p.and(q.zone.id.eq(webModel.getZone().getId()));
		}
		
		PageRequest pageRequest =
	            new PageRequest(pageNum -1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.ASC, "description");
		
		Page<Behavior> behaviors = behaviorRepo.findAll(p, pageRequest); 
		
		for(Behavior behavior : behaviors) {
			behavior.getImpacts().size();
		}
		
		ResponseJSend<Page<Behavior>> response = new ResponseJSend<Page<Behavior>>();
		response.data=behaviors;
		response.status=ResponseStatus.SUCCESS;
		
		return response;
	}

	@Override
	public ResponseJSend<Long> saveBehavior(JsonNode node, SecurityUser user) throws JsonMappingException {
		
		ObjectMapper mapper = getObjectMapper();
		
		
		Behavior webModel;
		
		try {
			webModel = mapper.treeToValue(node, Behavior.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonMappingException(e.getMessage() + "\n  JSON: " + node.toString());
		}
		
		Behavior dbModel = null;
				
		if(webModel.getId() == null) {
			dbModel = new Behavior();
			
			dbModel.setCreateBy(user);
			dbModel.setCreateDate(new Date());
			
			
			
		} else {
			dbModel = behaviorRepo.findOne(webModel.getId());
		}
		
		logger.debug(dbModel.getCreateBy().getUsername());
		

		BeanUtils.copyProperties(webModel, dbModel, "impacts", "createBy", "createDate");
		
		logger.debug("webModel situationID: " + webModel.getSituation().getId());
		
		Situation situation = null;
		if(webModel.getSituation()!=null && webModel.getSituation().getId() != null) {
			 situation = situationRepo.findOne(webModel.getSituation().getId());
		}
		
		
		dbModel.setLastUpdateBy(user);
		dbModel.setLastUpdateDate(new Date());
		dbModel.setSituation(situation);
		behaviorRepo.save(dbModel);
		
		
		List<BehaviorImpact> oldImpacts = dbModel.getImpacts();
		
		dbModel.setImpacts(new ArrayList<BehaviorImpact>());
		
		
		
		// now deal with impacts
		for(BehaviorImpact webImpact: webModel.getImpacts()) {
			logger.debug("saving impact: " + webImpact.getDescription());
			
			BehaviorImpact dbImpact;
			if(webImpact.getId() == null) {
				dbImpact = new BehaviorImpact();
				dbImpact.setCreateBy(user);
				dbImpact.setCreateDate(new Date());
			} else {
				dbImpact = behaviorImpactRepo.findOne(webImpact.getId());
			}
			
			BeanUtils.copyProperties(webImpact, dbImpact, "behavior", "createBy", "createDate");
			
			dbImpact.setLastUpdateBy(user);
			dbImpact.setLastUpdateDate(new Date());
			dbImpact.setBehavior(dbModel);
			
			if(oldImpacts!=null) {
				oldImpacts.remove(dbImpact);
			}
			
			dbModel.getImpacts().add(dbImpact);
		}
		
		if(oldImpacts != null) {
			behaviorImpactRepo.delete(oldImpacts);
		}
		
		behaviorRepo.save(dbModel);
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.status = ResponseStatus.SUCCESS;
		response.data = dbModel.getId(); 
		return response;
	}

	@Override
	public ResponseJSend<Long> deleteBehavior(Long id) {
		Behavior behavior = behaviorRepo.findOne(id);
		
		if(behavior != null) {
			behaviorRepo.delete(behavior);
		}
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.data = id;
		response.status = ResponseStatus.SUCCESS;
		
		return response;
	}

	@Override
	public ResponseJSend<Page<Journal>> findJournalByExample(JsonNode node,
			Integer pageNum, SecurityUser user) throws JsonMappingException {
		logger.debug("findJournalByExample");
		
//		Long orgId = node.get("organization").get("id").asLong();
//		OrganizationNetwork org = organizationNetworkRepo.findOne(orgId);
		
		ObjectNode object = (ObjectNode) node;
		object.remove("organization");
		Journal webModel;
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		
		try {
			webModel = mapper.treeToValue(object, Journal.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonMappingException(e.getMessage() + "\n  JSON: " + node.toString());
		}
		
		QJournal q = QJournal.journal;
		
		BooleanBuilder p = new BooleanBuilder();
		
		
		if(webModel.getJournalType() != null) {
			logger.debug("webModel.getJournalType: " +webModel.getJournalType().getId());
		}
		
		
		BooleanBuilder nameP = new BooleanBuilder();
		
		logger.debug("Searching for nameTh:  " + webModel.getNameTh());
		if(webModel.getNameTh() != null ) {
			nameP = nameP.or(q.nameTh.like("%"+webModel.getNameTh().trim()+"%"));
		}
		
		if(webModel.getNameEn() != null) {
			nameP = nameP.or(q.nameEn.like("%"+webModel.getNameEn().trim()+"%"));
		}
		
		
		if(user.getRoles().toString().indexOf("ADMIN", 0) < 0 ) {

			if(webModel.getJournalType() != null && webModel.getJournalType().getCode().equals("1") ) {
				p = p.andAnyOf(q.journalType.id.eq(webModel.getJournalType().getId()), q.journalType.code.eq("2"));
			} else if (webModel.getJournalType() != null && webModel.getJournalType().getCode().equals("8") ) {
				p = p.andAnyOf(q.journalType.id.eq(webModel.getJournalType().getId()), q.journalType.code.eq("9"));
			} else if(webModel.getJournalType() != null) {
				p = p.and(q.journalType.id.eq(webModel.getJournalType().getId()));
			}
		} else {
			if(webModel.getJournalType() != null) {
				p = p.and(q.journalType.id.eq(webModel.getJournalType().getId()));
			}
		}
		
		p = p.and(nameP);
		
		PageRequest pageRequest =
	            new PageRequest(pageNum -1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.ASC, "nameTh");
		
		Page<Journal> journal = journalRepo.findAll(p, pageRequest); 
		
		ResponseJSend<Page<Journal>> response = new ResponseJSend<Page<Journal>>();
		response.data=journal;
		response.status=ResponseStatus.SUCCESS;
		
		return response;
	}

	@Override
	public Journal findJournalById(Long id) {
		Journal journal = journalRepo.findOne(id);
		
		journal.getFiles().size();
		
		return journal;
	}

	private ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		mapper.setDateFormat(sdf);
		return mapper;
	}
	
	@Override
	public ResponseJSend<Long> saveJournal(JsonNode node, SecurityUser user) throws JsonMappingException {
		ObjectMapper mapper = getObjectMapper();
		
		ObjectNode object = (ObjectNode) node;
		object.remove("organization");
		object.remove("createBy");
		object.remove("lastUpdateBy");
		
		Journal webModel;
		
		try {
			webModel = mapper.treeToValue(object, Journal.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonMappingException(e.getMessage() + "\n  JSON: " + node.toString());
		}
		
		Journal dbModel = null;
				
		if(webModel.getId() == null) {
			dbModel = new Journal();
			
			dbModel.setCreateBy(user);
			dbModel.setCreateDate(new Date());
			
			
			
		} else {
			dbModel = journalRepo.findOne(webModel.getId());
		}
		
		BeanUtils.copyProperties(webModel, dbModel, "createBy", "createDate");

		if(webModel.getJournalType() != null) {
			JournalType type = (JournalType) domainVariableRepo.findOne(webModel.getJournalType().getId());
			dbModel.setJournalType(type);
			
		}
		
		dbModel.setLastUpdateBy(user);
		dbModel.setLastUpdateDate(new Date());
		journalRepo.save(dbModel);
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.status = ResponseStatus.SUCCESS;
		response.data = dbModel.getId(); 
		return response;
		
	}

	@Override
	public ResponseJSend<Long> deleteJournal(Long id) {
		Journal journal = journalRepo.findOne(id);
		
		if(journal != null) {
			journalRepo.delete(journal);
		}
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.data = id;
		response.status = ResponseStatus.SUCCESS;
		
		return response;
	}

	@Override
	public ResponseJSend<Page<Research>> findResearchByExample(JsonNode node,
			Integer pageNum) throws JsonMappingException {
		logger.debug("findResearchByExample");
		
//		Long orgId = node.get("organization").get("id").asLong();
//		OrganizationNetwork org = organizationNetworkRepo.findOne(orgId);
		
		ObjectNode object = (ObjectNode) node;
		object.remove("organization");
		Research webModel;
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		
		try {
			webModel = mapper.treeToValue(object, Research.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonMappingException(e.getMessage() + "\n  JSON: " + node.toString());
		}
		
		QResearch q = QResearch.research;
		
		BooleanBuilder p = new BooleanBuilder();
		
		if(webModel.getJournalType() != null) {
			logger.debug("Searching for Journal Type:  " + webModel.getJournalType());
			p = p.and(q.journalType.eq(webModel.getJournalType()));
		}
		
		BooleanBuilder nameP = new BooleanBuilder();
		if(webModel.getNameTh() != null) {
			nameP = nameP.or(q.nameTh.like(""+webModel.getNameTh().trim()+""));
		}
		
		if(webModel.getNameEn() != null) {
			nameP = nameP.or(q.nameEn.like(""+webModel.getNameEn().trim()+""));
		}
		
		p = p.and(nameP);
		
		PageRequest pageRequest =
	            new PageRequest(pageNum -1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.ASC, "nameTh");
		
		Page<Research> research = researchRepo.findAll(p, pageRequest); 
		
		ResponseJSend<Page<Research>> response = new ResponseJSend<Page<Research>>();
		response.data=research;
		response.status=ResponseStatus.SUCCESS;
		
		return response;
	}

	@Override
	public Research findResearchById(Long id) {
		Research research =researchRepo.findOne(id);
		research.getFiles().size();
		research.getJournalType().getId();
		
		return research;
	}

	@Override
	public ResponseJSend<Long> saveResearch(JsonNode node, SecurityUser user) throws JsonMappingException {
		ObjectMapper mapper = getObjectMapper();
		
		Long orgId = node.path("organization").path("id").asLong();
		OrganizationNetwork org = organizationNetworkRepo.findOne(orgId);
		
		
		ObjectNode object = (ObjectNode) node;
		object.remove("organization");
		object.remove("createBy");
		object.remove("lastUpdateBy");
		Research webModel;
		
		try {
			webModel = mapper.treeToValue(object, Research.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonMappingException(e.getMessage() + "\n  JSON: " + node.toString());
		}
		
		Research dbModel = null;
				
		if(webModel.getId() == null) {
			dbModel = new Research();
			
			dbModel.setCreateBy(user);
			dbModel.setCreateDate(new Date());
			
			
			
		} else {
			dbModel = researchRepo.findOne(webModel.getId());
		}
		
		BeanUtils.copyProperties(webModel, dbModel, "createBy", "createDate");

		dbModel.setOrganization(org);
		dbModel.setLastUpdateBy(user);
		dbModel.setLastUpdateDate(new Date());
		researchRepo.save(dbModel);
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.status = ResponseStatus.SUCCESS;
		response.data = dbModel.getId(); 
		return response;
		
	}

	@Override
	public ResponseJSend<Long> deleteResearch(Long id) {
		Research research = researchRepo.findOne(id);
		
		if(research != null) {
			researchRepo.delete(research);
		}
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.data = id;
		response.status = ResponseStatus.SUCCESS;
		
		return response;
	}

	@Override
	public ResponseJSend<Page<Situation>> findSituationByExample(JsonNode node,
			Integer pageNum) throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		
		
		
		ObjectNode object = (ObjectNode) node;
		object.remove("organization");
		Situation webModel;
		
		try {
			webModel = mapper.treeToValue(object, Situation.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonMappingException(e.getMessage() + "\n  JSON: " + node.toString());
		}
		
		QSituation q = QSituation.situation;
		
		BooleanBuilder p = new BooleanBuilder();
		
		if(webModel.getSituationType() != null) {
			logger.debug("Searching for Situation Type:  " + webModel.getSituationType());
			p = p.and(q.situationType.eq(webModel.getSituationType()));
		}
		
		if(webModel.getCode() != null) {
			p = p.and(q.code.like("%"+webModel.getCode().trim()+"%"));
		}
		
		if(webModel.getName() != null) {
			p = p.and(q.name.like("%"+webModel.getName().trim()+"%"));
		}
		
		if(webModel.getType() != null) {
			p = p.and(q.type.like("%"+webModel.getType().trim()+"%"));
		}
		
		
		PageRequest pageRequest =
	            new PageRequest(pageNum -1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.ASC, "code");
		
		Page<Situation> situation = situationRepo.findAll(p, pageRequest); 
		
		ResponseJSend<Page<Situation>> response = new ResponseJSend<Page<Situation>>();
		response.data=situation;
		response.status=ResponseStatus.SUCCESS;
		
		return response;
		
	}

	@Override
	public Situation findSituationById(Long id) {
		return situationRepo.findOne(id);
	}

	
	
	@Override
	public List<Situation> findAllSituation() {
		return situationRepo.findAll();
	}

	@Override
	public ResponseJSend<Long> saveSituation(JsonNode node, SecurityUser user) throws JsonMappingException {
		ObjectMapper mapper = getObjectMapper();
		
		Long situationTypeId = node.path("situationType").path("id").asLong();
		SituationType situationType = situationTypeRepo.findOne(situationTypeId);
		
		
		ObjectNode object = (ObjectNode) node;
		object.remove("organization");
		Situation webModel;
		
		try {
			webModel = mapper.treeToValue(object, Situation.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonMappingException(e.getMessage() + "\n  JSON: " + node.toString());
		}
		
		Situation dbModel = null;
				
		if(webModel.getId() == null) {
			dbModel = new Situation();
		} else {
			dbModel = situationRepo.findOne(webModel.getId());
		}
		
		dbModel.setCode(webModel.getCode());
		dbModel.setSituationType(situationType);
		dbModel.setName(webModel.getName());
		
		logger.debug(node.toString());
		logger.debug(webModel.getType());
		dbModel.setType(webModel.getType());
		
		
		situationRepo.save(dbModel);
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.status = ResponseStatus.SUCCESS;
		response.data = dbModel.getId(); 
		return response;
	}

	@Override
	public ResponseJSend<Long> deleteSituation(Long id) {
		Situation situation = situationRepo.findOne(id);
		
		if(situation != null) {
			situationRepo.delete(situation);
		}
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.data = id;
		response.status = ResponseStatus.SUCCESS;
		
		return response;
	}

	@Override
	public ResponseJSend<Page<JournalSituation>> findJournalSituationByExample(
			JsonNode node, Integer pageNum) {
		
		JournalSituation webModel = new JournalSituation();
		
		if(node.path("situation").path("id") != null) {
			Situation situation = situationRepo.findOne(node.path("situation").path("id").asLong());
			webModel.setSituation(situation);
		}
		
		if(node.path("journal").path("id") != null) {
			Journal journal = journalRepo.findOne(node.path("journal").path("id").asLong());
			webModel.setJournal(journal);
		}
		
		QJournalSituation q = QJournalSituation.journalSituation;
		
		BooleanBuilder p = new BooleanBuilder();
		
		
		if(webModel.getSituation() != null) {
			logger.debug("Searching for Situation :  " + webModel.getSituation().getId());
			p = p.and(q.situation.eq(webModel.getSituation()));
		}
		
		if(webModel.getJournal() != null) {
			p = p.and(q.journal.eq(webModel.getJournal()));
		}
		
		
		PageRequest pageRequest =
	            new PageRequest(pageNum -1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.ASC, "situation.id");
		
		Page<JournalSituation> journalSituations = journalSituationRepo.findAll(p, pageRequest); 
		
		ResponseJSend<Page<JournalSituation>> response = new ResponseJSend<Page<JournalSituation>>();
		response.data=journalSituations;
		response.status=ResponseStatus.SUCCESS;
		
		return response;
	}

	@Override
	public JournalSituation findJournalSituationById(Long id) {
		return journalSituationRepo.findOne(id);
	}

	@Override
	public ResponseJSend<Long> saveJournalSituation(JsonNode node,
			SecurityUser user) {
		
		JournalSituation dbModel = null;
		if(node.path("id") == null  || node.path("id").asLong() == 0L) {
			dbModel = new JournalSituation();
		} else {
			dbModel = journalSituationRepo.findOne(node.path("id").asLong());
		}
		
		if(node.path("situation").path("id") != null) {
			Situation situation = situationRepo.findOne(node.path("situation").path("id").asLong());
			dbModel.setSituation(situation);
		}
		
		if(node.path("journal").path("id") != null) {
			Journal journal = journalRepo.findOne(node.path("journal").path("id").asLong());
			dbModel.setJournal(journal);
		}
		
		journalSituationRepo.save(dbModel);
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.status = ResponseStatus.SUCCESS;
		response.data = dbModel.getId(); 
		return response;
			
	}

	@Override
	public ResponseJSend<Long> deleteJournalSituation(Long id) {
		JournalSituation journalSituation = journalSituationRepo.findOne(id);
		
		if(journalSituation != null) {
			journalSituationRepo.delete(journalSituation);
		}
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.data = id;
		response.status = ResponseStatus.SUCCESS;
		
		return response;
	}
	
	@Override
	public ResponseJSend<Page<ResearchSituation>> findResearchSituationByExample(
			JsonNode node, Integer pageNum) {
		
		ResearchSituation webModel = new ResearchSituation();
		
		if(node.path("situation").path("id") != null) {
			Situation situation = situationRepo.findOne(node.path("situation").path("id").asLong());
			webModel.setSituation(situation);
		}
		
		if(node.path("research").path("id") != null) {
			Research research = researchRepo.findOne(node.path("research").path("id").asLong());
			webModel.setResearch(research);
		}
		
		QResearchSituation q = QResearchSituation.researchSituation;
		
		BooleanBuilder p = new BooleanBuilder();
		
		
		if(webModel.getSituation() != null) {
			logger.debug("Searching for Situation :  " + webModel.getSituation().getId());
			p = p.and(q.situation.eq(webModel.getSituation()));
		}
		
		if(webModel.getResearch() != null) {
			p = p.and(q.research.eq(webModel.getResearch()));
		}
		
		
		PageRequest pageRequest =
	            new PageRequest(pageNum -1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.ASC, "situation.id");
		
		Page<ResearchSituation> researchSituation = researchSituationRepo.findAll(p, pageRequest); 
		
		ResponseJSend<Page<ResearchSituation>> response = new ResponseJSend<Page<ResearchSituation>>();
		response.data=researchSituation;
		response.status=ResponseStatus.SUCCESS;
		
		return response;
	}

	@Override
	public ResearchSituation findResearchSituationById(Long id) {
		return researchSituationRepo.findOne(id);
	}

	@Override
	public ResponseJSend<Long> saveResearchSituation(JsonNode node,
			SecurityUser user) {
		
		ResearchSituation dbModel = null;
		if(node.path("id") == null || node.path("id").asLong() == 0L ) {
			logger.debug("create new dbModel");
			dbModel = new ResearchSituation();
		} else {
			logger.debug("find dbModel with id=" + node.path("id").asLong());
			dbModel = researchSituationRepo.findOne(node.path("id").asLong());
		}
		
		if(node.path("situation").path("id") != null) {
			Situation situation = situationRepo.findOne(node.path("situation").path("id").asLong());
			dbModel.setSituation(situation);
		}
		
		if(node.path("research").path("id") != null) {
			Research research = researchRepo.findOne(node.path("research").path("id").asLong());
			dbModel.setResearch(research);
		}
		
		researchSituationRepo.save(dbModel);
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.status = ResponseStatus.SUCCESS;
		response.data = dbModel.getId(); 
		return response;
			
	}

	@Override
	public ResponseJSend<Long> deleteResearchSituation(Long id) {
		ResearchSituation researchSituation = researchSituationRepo.findOne(id);
		
		if(researchSituation != null) {
			researchSituationRepo.delete(researchSituation);
		}
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.data = id;
		response.status = ResponseStatus.SUCCESS;
		
		return response;
	}

	
	
	
	@Override
	public Iterable<PsychoSocialReport> findPsychoSocialReportByExample(
			PsychoSocialReport reportExample) {
		
		PsychoSocialReport webModel = reportExample;
		
		logger.debug("findPsychoSocialReportByExample");

		QPsychoSocialReport q = QPsychoSocialReport.psychoSocialReport;
		BooleanBuilder p = new BooleanBuilder();
		
		if(webModel.getOrganization()!=null) {
			logger.debug("webModel.getOrganization != null");
			
			p = p.and(q.organization.zone.id.eq(webModel.getOrganization().getZone().getId()));
			
			
			if(webModel.getBeginReportDate() != null && webModel.getEndReportDate() != null) {
				p = p
						.and(q.beginReportDate.between(webModel.getBeginReportDate(), webModel.getEndReportDate()))
						.and(q.endReportDate.between(webModel.getBeginReportDate(), webModel.getEndReportDate()));
			} else if(webModel.getBeginReportDate() != null) {
				p = p.and(q.beginReportDate.after(webModel.getBeginReportDate()));
			} else if(webModel.getEndReportDate() != null) {
				p = p.and(q.endReportDate.before(webModel.getEndReportDate()));
			}
			
		}
		
		
		Iterable<PsychoSocialReport> reports = psychoSocailReportRepo.findAll(p,
				QPsychoSocialReport.psychoSocialReport.organization.zone.id.asc(),
				QPsychoSocialReport.psychoSocialReport.organization.province.id.asc(),
				QPsychoSocialReport.psychoSocialReport.organization.id.asc()); 
		
		for(PsychoSocialReport report: reports) {
			if(report.getReportUser() != null) {
				report.getReportUser().getUsername();
				logger.debug(report.getReportUser().getUsername());
			}
		}
		
		
		
		return reports;
	}

	@Override
	public ResponseJSend<Page<PsychoSocialReport>> findPsychoSocialReportByExample(
			JsonNode node, Integer pageNum) throws JsonMappingException {
		
		PsychoSocialReport webModel;
		
		ObjectMapper mapper = getObjectMapper();
		
		try {
			webModel = mapper.treeToValue(node, PsychoSocialReport.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonMappingException(e.getMessage() + "\n  JSON: " + node.toString());
		}
		
		
		logger.debug("findPsychoSocialReportByExample");

		QPsychoSocialReport q = QPsychoSocialReport.psychoSocialReport;
		BooleanBuilder p = new BooleanBuilder();
		
		if(webModel.getOrganization()!=null) {
		
			if(webModel.getOrganization().getId() != 0) {
				p = p.and(q.organization.id.eq(webModel.getOrganization().getId()));
			} else if (webModel.getOrganization().getProvince() != null && webModel.getOrganization().getProvince().getId() != 0) {
				p = p.and(q.organization.province.id.eq(webModel.getOrganization().getProvince().getId()));
			} else if (webModel.getOrganization().getZone()!= null && webModel.getOrganization().getZone().getId() != 0) {
				p = p.and(q.organization.zone.id.eq(webModel.getOrganization().getZone().getId()));
			}
			
			if(webModel.getBeginReportDate() != null && webModel.getEndReportDate() != null) {
				p = p
						.and(q.beginReportDate.between(webModel.getBeginReportDate(), webModel.getEndReportDate()))
						.and(q.endReportDate.between(webModel.getBeginReportDate(), webModel.getEndReportDate()));
			} else if(webModel.getBeginReportDate() != null) {
				p = p.and(q.beginReportDate.after(webModel.getBeginReportDate()));
			} else if(webModel.getEndReportDate() != null) {
				p = p.and(q.endReportDate.before(webModel.getEndReportDate()));
			}
			
		}
		
		PageRequest pageRequest =
	            new PageRequest(pageNum -1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.DESC, "id");
		
		Page<PsychoSocialReport> reports = psychoSocailReportRepo.findAll(p, pageRequest); 
		
		for(PsychoSocialReport report: reports) {
			if(report.getReportUser() != null) {
				report.getReportUser().getUsername();
				logger.debug(report.getReportUser().getUsername());
			}
		}
		
		ResponseJSend<Page<PsychoSocialReport>> response = new ResponseJSend<Page<PsychoSocialReport>>();
		response.data=reports;
		response.status=ResponseStatus.SUCCESS;
		
		return response;

	}

	@Override
	public PsychoSocialReport findPsychoSocialReportById(Long id) {
		PsychoSocialReport report = psychoSocailReportRepo.findOne(id);
		report.getOrganization().getId();
		report.getOrganization().getProvince().getId();
		report.getOrganization().getZone().getId();
		if(report.getReportUser() != null) {
			report.getReportUser().getId();
		}
		
		return report;
	}

	@Override
	public ResponseJSend<Long> savePsychoSocialReport(JsonNode node,
			SecurityUser user) throws JsonMappingException {

		PsychoSocialReport dbModel = null;
		if(node.path("id") == null || node.path("id").asLong() == 0L ) {
			logger.debug("create new dbModel");
			dbModel = new PsychoSocialReport();
		} else {
			logger.debug("find dbModel with id=" + node.path("id").asLong());
			dbModel = psychoSocailReportRepo.findOne(node.path("id").asLong());
		}

		Long orgId = node.path("organization").path("id").asLong();
		
		logger.debug("orgId: " + orgId);
		OrganizationNetwork organization = organizationNetworkRepo.findOne(orgId);
		
		
		ObjectMapper mapper = getObjectMapper();
		ObjectNode object = (ObjectNode) node;
		object.remove("organization");
		PsychoSocialReport webModel;
		try {
			webModel = mapper.treeToValue(object, PsychoSocialReport.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonMappingException(e.getMessage() + "\n  JSON: " + node.toString());
		}

		BeanUtils.copyProperties(webModel, dbModel, "organization");
		
		dbModel.setOrganization(organization);
		dbModel.setReportDate(new Date());
		dbModel.setReportUser(user);
		
		
		psychoSocailReportRepo.save(dbModel);
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.status = ResponseStatus.SUCCESS;
		response.data = dbModel.getId(); 
		return response;

	}

	@Override
	public ResponseJSend<Long> deletePsychoSocialReport(Long id) {
		PsychoSocialReport report = psychoSocailReportRepo.findOne(id);
		
		if(report != null) {
			psychoSocailReportRepo.delete(report);
		}
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.data = id;
		response.status = ResponseStatus.SUCCESS;
		
		return response;
	}

	@Override
	public List<DomainCountTuple> findHistoryCountBetween(String domain, String start, String end) {
		try {
			Date startDate = ddmmyyyyFormat.parse(start);
			Date endDate = ddmmyyyyFormat.parse(end);
			
			logger.debug(start + " : " +startDate);
			logger.debug(end + " : " +endDate);
			
			List<Object[]> result = fileHistoryRecordRepo.countStat(domain, start, end);
		
			List<JournalType> types = journalTypeRepo.findAll();
			HashMap<Long, DomainCountTuple> journalTypeMap = new HashMap<Long, DomainCountTuple>();
			List<DomainCountTuple> returnList = new ArrayList<DomainCountTuple>();
			
			for(JournalType t: types) {
				DomainCountTuple tuple = new DomainCountTuple();
				tuple.setDomainName(t.getDescription()); 
				tuple.setCount(0);
				journalTypeMap.put(t.getId(), tuple);
				returnList.add(tuple);
			}
			
			for(Object[] row: result) {
				
				
				DomainCountTuple tuple = journalTypeMap.get(((BigDecimal) row[0]).toBigInteger().longValue());
				if(tuple != null) {
					logger.debug( ((BigDecimal) row[0]).toString() );
					logger.debug(" >" + (BigDecimal) row[1]);
					
					tuple.setCount(((BigDecimal) row[1]).toBigInteger().intValue());
				}
			}
			
			return returnList;
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
}
