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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import smt.auth.model.Activeuser;
import smt.auth.model.SecurityUser;
import smt.model.Situation;
import smt.service.EntityService;
import smt.webUI.DomainCountTuple;
import smt.webUI.ResponseJSend;

@RestController
@RequestMapping("/REST/FileHistoryRecord")
public class FileHistoryRecordRestController {

public static Logger logger = LoggerFactory.getLogger(FileHistoryRecordRestController.class);
	
	@Autowired
	private EntityService entityService;
	
	@RequestMapping(value="/stat/{domain}/findHistoryCountBetween") 
	public Iterable<DomainCountTuple> findHistoryCountBetween(
			@PathVariable String domain,
			@RequestParam String start,
			@RequestParam String end) {
		logger.debug("domain: "+ domain);
		logger.debug("start: "+ start);
		logger.debug("end: "+ end);
		return entityService.findHistoryCountBetween(domain, start, end);
	}
	
	
}
