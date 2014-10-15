package smt.service;

import org.springframework.data.jpa.repository.JpaRepository;

import smt.model.glb.NetworkType;

public interface NetworkTypeRepo extends JpaRepository<NetworkType, Long> {

}
