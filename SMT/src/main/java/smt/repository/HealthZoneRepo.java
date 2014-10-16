package smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import smt.model.glb.HealthZone;

public interface HealthZoneRepo extends JpaRepository<HealthZone, Long> {

}
