package smt.controller.rest;

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
import smt.model.OrganizationNetwork;
import smt.service.EntityService;
import smt.webUI.ResponseJSend;

@RestController
@RequestMapping("/REST/OrganizationNetwork")
public class OrganizationNetworkRestController {
	
	@Autowired
	private EntityService entityService;
	
	
	
	@RequestMapping(value = "/search/page/{pageNum}", method = {RequestMethod.POST}) 
	public ResponseJSend<Page<OrganizationNetwork>> findOrganizationNetworkByExample(
			@RequestBody JsonNode node, @Activeuser SecurityUser user, @PathVariable Integer pageNum) {
		return entityService.findOrganizationNetworkByExample(node, pageNum);
	}

	@RequestMapping(value= "/{id}", method = {RequestMethod.GET})
	public OrganizationNetwork findOrganizationNetworkById(@PathVariable Long id) {
		return entityService.findOrganizationNetworkById(id);
	}
	
	@RequestMapping(value = "", method = {RequestMethod.POST}) 
	public ResponseJSend<Long> saveOrganizationNetwork(@RequestBody JsonNode node, @Activeuser SecurityUser user) {
		
		return this.entityService.saveOrganizationNetwork(node, user);
	}
	
	
}


