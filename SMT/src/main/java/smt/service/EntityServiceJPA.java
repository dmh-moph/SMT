package smt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import smt.model.glb.Province;
import smt.repository.ProvinceRepo;

public class EntityServiceJPA implements EntityService {

	@Autowired
	ProvinceRepo provinceRepo;
	
	@Override
	public List<Province> findAllProvince() {
		
		return provinceRepo.findAll();
	}

}
