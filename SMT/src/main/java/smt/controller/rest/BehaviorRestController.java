package smt.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import smt.auth.model.Activeuser;
import smt.auth.model.SecurityUser;
import smt.model.Behavior;
import smt.service.EntityService;
import smt.webUI.ResponseJSend;

@RestController
@RequestMapping("/REST/Behavior")
public class BehaviorRestController {

public static Logger logger = LoggerFactory.getLogger(BehaviorRestController.class);
	
	@Autowired
	private EntityService entityService;
	
	@RequestMapping(value = "/search/page/{pageNum}", method = {RequestMethod.POST}) 
	public ResponseJSend<Page<Behavior>> findBehaviorByExample(
			@RequestBody JsonNode node, @Activeuser SecurityUser user, @PathVariable Integer pageNum) {
		return entityService.findBehaviorByExample(node, pageNum);
	}

	@RequestMapping(value= "/{id}", method = {RequestMethod.GET})
	public Behavior findBehaviorById(@PathVariable Long id) {
		return entityService.findBehaviorById(id);
	}
	
	@RequestMapping(value= "/{id}", method = {RequestMethod.PUT})
	public ResponseJSend<Long> updateBehaviorById(@RequestBody JsonNode node,
			@Activeuser SecurityUser user) throws JsonMappingException {
		return entityService.saveBehavior(node, user);
	}
	
	@RequestMapping(value= "/{id}", method = {RequestMethod.DELETE})
	public ResponseJSend<Long> deleteBehaviorById(
			@PathVariable Long id,
			@Activeuser SecurityUser user) {
		return entityService.deleteBehavior(id);
	}
	
	
	@RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.PUT}) 
	public ResponseJSend<Long> saveBehavior(@RequestBody JsonNode node,
			@Activeuser SecurityUser user) throws JsonMappingException {
		
		return this.entityService.saveBehavior(node, user);
	}
	
}
