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
import smt.webUI.DefaultProperty;
import smt.webUI.ResponseJSend;
import smt.webUI.ResponseStatus;

@Transactional
@Service
public class SecUserEntityServiceJPA implements SecUserEntityService {

	public static Logger logger = LoggerFactory.getLogger(SecUserEntityServiceJPA.class);
	
	@Autowired
	private SecUserRepository secUserRepo;
	
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
}
