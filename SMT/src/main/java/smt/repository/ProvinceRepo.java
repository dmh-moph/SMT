package smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import smt.model.glb.Province;

public interface ProvinceRepo extends JpaRepository<Province, Long> {

}
