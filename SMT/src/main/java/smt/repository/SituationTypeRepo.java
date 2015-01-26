package smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import smt.model.glb.SituationType;

public interface SituationTypeRepo extends JpaRepository<SituationType, Long> {

}
