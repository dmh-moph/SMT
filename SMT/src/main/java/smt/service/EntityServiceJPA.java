package smt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import com.fasterxml.jackson.databind.JsonNode;

import smt.auth.model.SecurityUser;
import smt.auth.service.SecUserRepository;
import smt.model.OrganizationNetwork;
import smt.model.glb.Amphur;
import smt.model.glb.DomainVariable;
import smt.model.glb.HealthZone;
import smt.model.glb.NetworkType;
import smt.model.glb.OrgType;
import smt.model.glb.Province;
import smt.repository.DomainVariableRepo;
import smt.repository.OrganizationNetworkRepo;
import smt.repository.ProvinceRepo;
import smt.webUI.ResponseJSend;
import smt.webUI.ResponseStatus;

public class EntityServiceJPA implements EntityService {

	@Autowired
	ProvinceRepo provinceRepo;
	
	@Autowired
	DomainVariableRepo domainVariableRepo;
	
	@Autowired
	OrganizationNetworkRepo organizationNetworkRepo;
	
	@Autowired
	HealthZoneRepo healthZoneRepo;

	@Autowired
	AmphurRepo amphurRepo;
	
	@Autowired
	NetworkTypeRepo networkTypeRepo;
	
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
		SecurityUser sUser = secUserRepo.findOne(1L);
		if(node.get("id") != null) {
			// this is update
			model = organizationNetworkRepo.findOne(node.get("id").asLong());
			
			model.setLastUpdateBy(sUser);
			model.setLastUpdateDate(new Date());
		} else {
			// this is new
			model = new OrganizationNetwork();
			model.setCreateBy(sUser);
			model.setCreateDate(new Date());
			model.setLastUpdateBy(sUser);
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
		
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.status = ResponseStatus.SUCCESS;
		response.data = model.getId(); 
		return response;
	}

	
	
}
