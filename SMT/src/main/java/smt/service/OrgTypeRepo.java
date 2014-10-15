package smt.service;

import org.springframework.data.jpa.repository.JpaRepository;

import smt.model.glb.OrgType;

public interface OrgTypeRepo extends JpaRepository<OrgType, Long> {

}
