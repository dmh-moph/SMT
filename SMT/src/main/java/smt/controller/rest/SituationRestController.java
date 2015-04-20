package smt.controller.rest;

import java.util.List;

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
import smt.model.Situation;
import smt.service.EntityService;
import smt.webUI.ResponseJSend;

@RestController
@RequestMapping("/REST/Situation")
public class SituationRestController {

public static Logger logger = LoggerFactory.getLogger(SituationRestController.class);
	
	@Autowired
	private EntityService entityService;
	
	@RequestMapping(value="/findAllSituation") 
	public List<Situation> findAllSituation() {
		return entityService.findAllSituation();
	}
	
	@RequestMapping(value = "/search/page/{pageNum}", method = {RequestMethod.POST}) 
	public ResponseJSend<Page<Situation>> findSituationByExample(
			@RequestBody JsonNode node, @Activeuser SecurityUser user, @PathVariable Integer pageNum) throws JsonMappingException {
		return entityService.findSituationByExample(node, pageNum);
	}

	@RequestMapping(value= "/{id}", method = {RequestMethod.GET})
	public Situation findSituationById(@PathVariable Long id) {
		return entityService.findSituationById(id);
	}
	
	@RequestMapping(value= "/{id}", method = {RequestMethod.PUT})
	public ResponseJSend<Long> updateSituationById(@RequestBody JsonNode node,
			@Activeuser SecurityUser user) throws JsonMappingException {
		return entityService.saveSituation(node, user);
	}
	
	@RequestMapping(value= "/{id}", method = {RequestMethod.DELETE})
	public ResponseJSend<Long> deleteSituationById(
			@PathVariable Long id,
			@Activeuser SecurityUser user) {
		return entityService.deleteSituation(id);
	}
	
	
	@RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.PUT}) 
	public ResponseJSend<Long> saveSituation(@RequestBody JsonNode node,
			@Activeuser SecurityUser user) throws JsonMappingException {
		
		return this.entityService.saveSituation(node, user);
	}
	
}
