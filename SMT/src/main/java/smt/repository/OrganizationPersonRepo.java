package smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import smt.model.OrganizationPerson;

public interface OrganizationPersonRepo extends JpaRepository<OrganizationPerson, Long> {

}
