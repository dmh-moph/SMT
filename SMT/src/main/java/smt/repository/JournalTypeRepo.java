package smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import smt.model.glb.JournalType;

public interface JournalTypeRepo extends JpaRepository<JournalType, Long> {

}
