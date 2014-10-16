package smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import smt.model.glb.PersonType;

public interface PersonTypeRepo extends JpaRepository<PersonType, Long> {

}
