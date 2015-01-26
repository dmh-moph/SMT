package smt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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
import smt.model.Journal;
import smt.model.OrganizationNetwork;
import smt.model.OrganizationPerson;
import smt.model.QBehavior;
import smt.model.QJournal;
import smt.model.QOrganizationNetwork;
import smt.model.QResearch;
import smt.model.Research;
import smt.model.glb.Amphur;
import smt.model.glb.DomainVariable;
import smt.model.glb.HealthZone;
import smt.model.glb.NetworkType;
import smt.model.glb.OrgType;
import smt.model.glb.PersonType;
import smt.model.glb.Province;
import smt.repository.AmphurRepo;
import smt.repository.BehaviorImpactRepo;
import smt.repository.BehaviorRepo;
import smt.repository.DomainVariableRepo;
import smt.repository.HealthZoneRepo;
import smt.repository.JournalRepo;
import smt.repository.NetworkTypeRepo;
import smt.repository.OrgTypeRepo;
import smt.repository.OrganizationNetworkRepo;
import smt.repository.OrganizationPersonRepo;
import smt.repository.PersonTypeRepo;
import smt.repository.ProvinceRepo;
import smt.webUI.DefaultProperty;
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
	PersonTypeRepo personTypeRepo;
	
	@Autowired
	OrgTypeRepo orgTypeRepo;
	
	@Autowired
	SecUserRepository secUserRepo;
	
	@Autowired
	JournalRepo journalRepo;
	
	@Autowired
	ResearchRepo researchRepo;
	
	@Override
	public List<Province> findAllProvince() {
		return provinceRepo.findAll();
	}

	@Override
	public List<Amphur> findAllAmphurByProvinceId(Long provinceId) {
		return provinceRepo.findAllAmphurByProvinceId(provinceId);
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
	public DomainVariable findDomainVariableByDomainNameAndId(
			String domainName, Long id) {
		return domainVariableRepo.findOne(id);
	}

	@Override
	public OrganizationNetwork findOrganizationNetworkById(Long id) {
		return organizationNetworkRepo.findOne(id);
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
	            new PageRequest(pageNum -1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.ASC, "orgName");

		
		QOrganizationNetwork q = QOrganizationNetwork.organizationNetwork;
		
		logger.debug("node: " + node.toString());
		
		String orgNameStr = "%";
		if(node.get("orgName") != null && node.get("orgName").asText().length() > 0) {
			orgNameStr += node.get("orgName").asText() + "%";
		}
		BooleanExpression p = q.orgName.like(orgNameStr);
		
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
		return behaviorRepo.findOne(id);
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
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
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
		dbModel.setLastUpdateBy(user);
		dbModel.setLastUpdateDate(new Date());
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
			Integer pageNum) throws JsonMappingException {
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
			logger.debug("Searching for Journal Type:  " + webModel.getJournalType());
			p = p.and(q.journalType.eq(webModel.getJournalType()));
		}
		
		if(webModel.getNameTh() != null) {
			p = p.and(q.nameTh.like(""+webModel.getNameTh().trim()+""));
		}
		
		if(webModel.getNameEn() != null) {
			p = p.and(q.nameEn.like(""+webModel.getNameEn().trim()+""));
		}
		
		
		
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
		return journalRepo.findOne(id);
	}

	@Override
	public ResponseJSend<Long> saveJournal(JsonNode node, SecurityUser user) throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		ObjectNode object = (ObjectNode) node;
		object.remove("organization");
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
		
		if(webModel.getNameTh() != null) {
			p = p.and(q.nameTh.like(""+webModel.getNameTh().trim()+""));
		}
		
		if(webModel.getNameEn() != null) {
			p = p.and(q.nameEn.like(""+webModel.getNameEn().trim()+""));
		}
		
		
		
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
		return researchRepo.findOne(id);
	}

	@Override
	public ResponseJSend<Long> saveResearch(JsonNode node, SecurityUser user) throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		Long orgId = node.path("organization").path("id").asLong();
		OrganizationNetwork org = organizationNetworkRepo.findOne(orgId);
		
		
		ObjectNode object = (ObjectNode) node;
		object.remove("organization");
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
	
	
	
}
