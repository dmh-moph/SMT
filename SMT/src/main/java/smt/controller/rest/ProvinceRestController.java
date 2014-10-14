package smt.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import smt.model.glb.Province;
import smt.service.EntityService;

@RestController
@RequestMapping("/REST/Province")
public class ProvinceRestController {
	
	@Autowired
	private EntityService entityService;
	
	@RequestMapping("") 
	public List<Province> findAllProvince() {
		
		return entityService.findAllProvince();
		
		
	}
	

}
