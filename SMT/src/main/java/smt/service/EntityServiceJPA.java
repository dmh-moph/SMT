package smt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.crsh.shell.impl.command.system.help;
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
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.expr.BooleanExpression;

import smt.auth.model.SecurityUser;
import smt.auth.service.SecUserRepository;
import smt.model.Behavior;
import smt.model.BehaviorImpact;
import smt.model.OrganizationNetwork;
import smt.model.OrganizationPerson;
import smt.model.QBehavior;
import smt.model.QOrganizationNetwork;
import smt.model.glb.Amphur;
import smt.model.glb.DomainVariable;
import smt.model.glb.EducationLevel;
import smt.model.glb.HealthZone;
import smt.model.glb.NetworkType;
import smt.model.glb.OrgType;
import smt.model.glb.PersonType;
import smt.model.glb.Province;
import smt.model.glb.SituationType;
import smt.repository.AmphurRepo;
import smt.repository.BehaviorRepo;
import smt.repository.DomainVariableRepo;
import smt.repository.HealthZoneRepo;
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
		model.setOrgCode1(node.get("orgCode1").asText());
		model.setOrgCode2(node.get("orgCode2").asText());
		model.setContactPerson(node.get("contactPerson").asText());
		model.setTelephone(node.get("telephone").asText());
		model.setEmail(node.get("email").asText());
		model.setWebsite(node.get("website").asText());
		

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
		
		dbModel.setImpacts(new ArrayList<BehaviorImpact>());
		
		behaviorRepo.save(dbModel);
		
		// now deal with impacts
		for(BehaviorImpact webImpact: webModel.getImpacts()) {
			logger.debug("saving impact: " + webImpact.getDescription());
			BehaviorImpact dbImpact = new BehaviorImpact();
			BeanUtils.copyProperties(webImpact, dbImpact, "behavior");
			
			if(dbImpact.getId() == null) {
				
				dbImpact.setCreateBy(user);
				dbImpact.setCreateDate(new Date());
			}
			
			dbImpact.setLastUpdateBy(user);
			dbImpact.setLastUpdateDate(new Date());
			dbImpact.setBehavior(dbModel);
			
			dbModel.getImpacts().add(dbImpact);
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

	
	
	
	
}
