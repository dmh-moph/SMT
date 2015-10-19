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
import smt.model.JournalSituation;
import smt.service.EntityService;
import smt.webUI.ResponseJSend;

@RestController
@RequestMapping("/REST/JournalSituation")
public class JournalSituationRestController {

public static Logger logger = LoggerFactory.getLogger(JournalSituationRestController.class);
	
	@Autowired
	private EntityService entityService;
	
	@RequestMapping(value = "/search/page/{pageNum}", method = {RequestMethod.POST}) 
	public ResponseJSend<Page<JournalSituation>> findJournalSituationByExample(
			@RequestBody JsonNode node, @Activeuser SecurityUser user, @PathVariable Integer pageNum) throws JsonMappingException {
		return entityService.findJournalSituationByExample(node, pageNum);
	}

	@RequestMapping(value= "/{id}", method = {RequestMethod.GET})
	public JournalSituation findJournalSituationById(@PathVariable Long id) {
		return entityService.findJournalSituationById(id);
	}
	
	@RequestMapping(value= "/{id}", method = {RequestMethod.PUT})
	public ResponseJSend<Long> updateJournalSituationById(@RequestBody JsonNode node,
			@Activeuser SecurityUser user) throws JsonMappingException {
		return entityService.saveJournalSituation(node, user);
	}
	
	@RequestMapping(value= "/{id}", method = {RequestMethod.DELETE})
	public ResponseJSend<Long> deleteJournalSituationById(
			@PathVariable Long id,
			@Activeuser SecurityUser user) {
		return entityService.deleteJournalSituation(id);
	}
	
	
	@RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.PUT}) 
	public ResponseJSend<Long> saveJournalSituation(@RequestBody JsonNode node,
			@Activeuser SecurityUser user) throws JsonMappingException {
		
		return this.entityService.saveJournalSituation(node, user);
	}
	
}
