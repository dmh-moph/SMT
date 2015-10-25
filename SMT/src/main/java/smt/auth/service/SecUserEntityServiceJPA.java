package smt.auth.service;

import java.util.HashSet;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import smt.auth.model.QSecurityUser;
import smt.auth.model.SecurityRole;
import smt.auth.model.SecurityUser;
import smt.auth.model.UserInfo;
import smt.model.glb.Occupation;
import smt.model.glb.Position;
import smt.model.glb.Sex;
import smt.model.glb.UserInfoObjective;
import smt.repository.DomainVariableRepo;
import smt.webUI.DefaultProperty;
import smt.webUI.ResponseJSend;
import smt.webUI.ResponseStatus;

@Transactional
@Service
public class SecUserEntityServiceJPA implements SecUserEntityService {

	public static Logger logger = LoggerFactory.getLogger(SecUserEntityServiceJPA.class);
	
	@Autowired
	private SecUserRepository secUserRepo;
	
	
	@Autowired
	DomainVariableRepo domainVariableRepo;
	
	@Override
	public SecurityUser getSecUserByLogin(String username) {
		SecurityUser sUser = secUserRepo.findUserByUserName(username);
		//logger.debug("sUser " + sUser.getSecurityRoles().size());
		sUser.getSecurityRoles().size();
		return sUser;
	}

	@Override
	public ResponseJSend<Page<SecurityUser>> findSecurityUserByExample(
			JsonNode node, Integer pageNum) {
		SecurityUser webModel = new SecurityUser();
		webModel.setUsername(node.path("username").asText());
		
		QSecurityUser q = QSecurityUser.securityUser;
		
		PageRequest pageRequest =
	            new PageRequest(pageNum -1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.ASC, "username");
		
		logger.debug("username: " + webModel.getUsername());
		
		Page<SecurityUser> users = secUserRepo.findAll(q.username.like("%" + webModel.getUsername() + "%"), pageRequest); 
		
		ResponseJSend<Page<SecurityUser>> response = new ResponseJSend<Page<SecurityUser>>();
		response.data=users;
		response.status=ResponseStatus.SUCCESS;
		
		return response;
	}

	@Override
	public ResponseJSend<Long> saveSecurityUser(JsonNode node, SecurityUser user) {
		
		SecurityUser webModel = new SecurityUser();
		webModel.setUsername(node.path("username").asText());
		webModel.setPassword(node.path("password").asText());
		
		SecurityUser dbModel;
		if(node.path("id").asLong() > 0) {
			dbModel = secUserRepo.findOne(node.path("id").asLong());
			
			
		} else {
			dbModel = new SecurityUser();
			SecurityRole userRole = secUserRepo.findUserRole();
			
			
			dbModel.setSecurityRoles(new HashSet<SecurityRole>());
			dbModel.getSecurityRoles().add(userRole);
		}
		
		dbModel.setUsername(webModel.getUsername());
		dbModel.setPassword(webModel.getPassword());
		
		UserInfo info;
		if(dbModel.getInfo() == null) {
			info = new UserInfo();
			info.setUser(dbModel);
			dbModel.setInfo(info);
		} else {
			info = dbModel.getInfo();
		}
		
		if(node.get("info") != null) {
			JsonNode infoNode = node.get("info");
			
			info.setDepartment(infoNode.path("department").asText());
			info.setEmail(infoNode.path("email").asText());
			
			if(infoNode.path("sex").asText().equals("M") ) {
				info.setSex(Sex.M);	
			} else if (infoNode.path("sex").asText().equals("F") ) {
				info.setSex(Sex.F);
			}
			
			if(infoNode.get("occupation") != null) {
				Occupation o = (Occupation) domainVariableRepo.findOne(infoNode.get("occupation").get("id").asLong());
				info.setOccupation(o);
			}
			info.setOccupationOther(infoNode.path("occupationOther").asText());
			
			if(infoNode.get("position") != null) {
				Position p = (Position) domainVariableRepo.findOne(infoNode.get("position").get("id").asLong());
				info.setPosition(p);
			}
			info.setPositionOther(infoNode.path("positionOther").asText());
			
			if(infoNode.get("objective") != null) {
				UserInfoObjective o = (UserInfoObjective) domainVariableRepo.findOne(infoNode.get("objective").get("id").asLong());
				info.setObjective(o);
			}
			info.setObjectiveOther(infoNode.path("objectiveOther").asText());
			
		}
 		
		secUserRepo.save(dbModel);
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.status = ResponseStatus.SUCCESS;
		response.data = dbModel.getId(); 
		return response;
	}

	@Override
	public ResponseJSend<Long> deleteSecurityUser(Long id) {
		SecurityUser dbModel;
		dbModel = secUserRepo.findOne(id);
		Long oldId = dbModel.getId();
		secUserRepo.delete(dbModel);
		
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		response.status = ResponseStatus.SUCCESS;
		response.data = oldId;
		return response;
	}

	@Override
	public SecurityUser findSecurityUserById(Long id) {
		return secUserRepo.findOne(id);
	}

	@Override
	public SecurityUser findSecurityUserByUsername(String username) {
			return secUserRepo.findUserByUserName(username);
	}
}
