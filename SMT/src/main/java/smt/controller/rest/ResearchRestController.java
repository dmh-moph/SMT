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
import smt.model.Research;
import smt.service.EntityService;
import smt.webUI.ResponseJSend;

@RestController
@RequestMapping("/REST/Research")
public class ResearchRestController {

public static Logger logger = LoggerFactory.getLogger(ResearchRestController.class);
	
	@Autowired
	private EntityService entityService;
	
	@RequestMapping(value = "/search/page/{pageNum}", method = {RequestMethod.POST}) 
	public ResponseJSend<Page<Research>> findResearchByExample(
			@RequestBody JsonNode node, @Activeuser SecurityUser user, @PathVariable Integer pageNum) throws JsonMappingException {
		return entityService.findResearchByExample(node, pageNum);
	}

	@RequestMapping(value= "/{id}", method = {RequestMethod.GET})
	public Research findResearchById(@PathVariable Long id) {
		return entityService.findResearchById(id);
	}
	
	@RequestMapping(value= "/{id}", method = {RequestMethod.PUT})
	public ResponseJSend<Long> updateResearchById(@RequestBody JsonNode node,
			@Activeuser SecurityUser user) throws JsonMappingException {
		return entityService.saveResearch(node, user);
	}
	
	@RequestMapping(value= "/{id}", method = {RequestMethod.DELETE})
	public ResponseJSend<Long> deleteResearchById(
			@PathVariable Long id,
			@Activeuser SecurityUser user) {
		return entityService.deleteResearch(id);
	}
	
	
	@RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.PUT}) 
	public ResponseJSend<Long> saveResearch(@RequestBody JsonNode node,
			@Activeuser SecurityUser user) throws JsonMappingException {
		
		return this.entityService.saveResearch(node, user);
	}
	
}
