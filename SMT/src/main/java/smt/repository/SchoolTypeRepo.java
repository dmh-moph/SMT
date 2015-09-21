package smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import smt.model.glb.NetworkType;
import smt.model.glb.SchoolType;

public interface SchoolTypeRepo extends JpaRepository<SchoolType, Long> {

}
