package smt.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import smt.model.glb.Amphur;
import smt.model.glb.HealthZone;
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
	
	@RequestMapping("/{provinceId}/Amphur")
	public List<Amphur> findAllAmphurByProvince(@PathVariable Long provinceId) {
		return entityService.findAllAmphurByProvinceId(provinceId);
	}
	
	@RequestMapping("/findAllZone")
	public List<HealthZone> findAllZone() {
		return entityService.findAllHealthZone();
	}
	
	@RequestMapping("/findAllByZone/{zoneId}")
	public List<Province> findAllByZoneId(@PathVariable Long zoneId) {
		return entityService.findAllProvinceByHealthZoneId(zoneId);
	}
	

}
