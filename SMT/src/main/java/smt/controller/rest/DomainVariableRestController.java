package smt.controller.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import smt.model.glb.DomainVariable;
import smt.service.EntityService;

@RestController
@RequestMapping("/REST/DomainVariable")
public class DomainVariableRestController {
	
	public static Logger logger = LoggerFactory.getLogger(DomainVariableRestController.class);
	
	@Autowired
	private EntityService entityService;
	
	
	@RequestMapping("/OCCUPATION/{id}/POSITION")
	public List<DomainVariable> findAllPostionByOCCUPATION(@PathVariable Long id) {
		logger.debug("OCCUPATION ID: " + id);
		return entityService.findAllDomainVariablePostionByOccupationId(id);
	}
	
	@RequestMapping("/{domainName}")
	public List<DomainVariable> findAllByDomanName(@PathVariable String domainName) {
		logger.debug("domainName: " + domainName);
		return entityService.findAllDomainVariableByDomainName(domainName);
	}
	
	@RequestMapping("/{domainName}/{id}")
	public DomainVariable findByDomanNameAndId(@PathVariable String domainName, @PathVariable Long id) {
		logger.debug("domainName: " + domainName + " id: " + id);
		return entityService.findDomainVariableByDomainNameAndId(domainName,id);
	}
	

}
