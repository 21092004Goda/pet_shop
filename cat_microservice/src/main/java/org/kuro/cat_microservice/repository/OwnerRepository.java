package org.kuro.cat_microservice.repository;

import org.kuro.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, UUID> {
    List<Owner> findByName(String name);
    List<Owner> findByDateOfBirth(Date dateOfBirth);
}