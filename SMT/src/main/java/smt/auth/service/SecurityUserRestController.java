package smt.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import smt.auth.model.Activeuser;
import smt.auth.model.SecurityUser;

import smt.webUI.ResponseJSend;

@RestController
@RequestMapping("/REST/SecurityUser")
public class SecurityUserRestController {
	
	@Autowired
	private SecUserEntityService secUserEntityService;
	
	
	@RequestMapping(value = "/search/page/{pageNum}", method = {RequestMethod.POST}) 
	public ResponseJSend<Page<SecurityUser>> findSecurityUserByExample(
			@RequestBody JsonNode node, @Activeuser SecurityUser user, @PathVariable Integer pageNum) {
		return secUserEntityService.findSecurityUserByExample(node, pageNum);
	}

	@RequestMapping(value= "/{id}", method = {RequestMethod.GET})
	public SecurityUser findSecurityUserById(@PathVariable Long id) {
		return secUserEntityService.findSecurityUserById(id);
	}
	
	@RequestMapping(value= "/{id}", method = {RequestMethod.PUT})
	public ResponseJSend<Long> updateSecurityUserById(@RequestBody JsonNode node,
			@Activeuser SecurityUser user) {
		return secUserEntityService.saveSecurityUser(node, user);
	}
	
	@RequestMapping(value= "/{id}", method = {RequestMethod.DELETE})
	public ResponseJSend<Long> deleteSecurityUserById(
			@PathVariable Long id,
			@Activeuser SecurityUser user) {
		return secUserEntityService.deleteSecurityUser(id);
	}
	
	
	@RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.PUT}) 
	public ResponseJSend<Long> saveSecurityUser(@RequestBody JsonNode node,
			@Activeuser SecurityUser user) {
		
		return this.secUserEntityService.saveSecurityUser(node, user);
	}
	
	
}


