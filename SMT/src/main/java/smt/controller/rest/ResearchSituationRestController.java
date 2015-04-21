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
import smt.model.ResearchSituation;
import smt.service.EntityService;
import smt.webUI.ResponseJSend;

@RestController
@RequestMapping("/REST/ResearchSituation")
public class ResearchSituationRestController {

public static Logger logger = LoggerFactory.getLogger(ResearchSituationRestController.class);
	
	@Autowired
	private EntityService entityService;
	
	@RequestMapping(value = "/search/page/{pageNum}", method = {RequestMethod.POST}) 
	public ResponseJSend<Page<ResearchSituation>> findResearchSituationByExample(
			@RequestBody JsonNode node, @Activeuser SecurityUser user, @PathVariable Integer pageNum) throws JsonMappingException {
		return entityService.findResearchSituationByExample(node, pageNum);
	}

	@RequestMapping(value= "/{id}", method = {RequestMethod.GET})
	public ResearchSituation findResearchSituationById(@PathVariable Long id) {
		return entityService.findResearchSituationById(id);
	}
	
	@RequestMapping(value= "/{id}", method = {RequestMethod.PUT})
	public ResponseJSend<Long> updateResearchSituationById(@RequestBody JsonNode node,
			@Activeuser SecurityUser user) throws JsonMappingException {
		return entityService.saveResearchSituation(node, user);
	}
	
	@RequestMapping(value= "/{id}", method = {RequestMethod.DELETE})
	public ResponseJSend<Long> deleteResearchSituationById(
			@PathVariable Long id,
			@Activeuser SecurityUser user) {
		return entityService.deleteResearchSituation(id);
	}
	
	
	@RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.PUT}) 
	public ResponseJSend<Long> saveResearchSituation(@RequestBody JsonNode node,
			@Activeuser SecurityUser user) throws JsonMappingException {
		
		return this.entityService.saveResearchSituation(node, user);
	}
	
}
