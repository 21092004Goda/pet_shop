package org.kuro.owner_microservice.repository;

import org.kuro.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CatRepository extends JpaRepository<Cat, UUID> {
    List<Cat> findByName(String name);
    List<Cat> findByBreed(String breed);
    List<Cat> findByColor(String color);
    List<Cat> findByBreedAndColor(String breed, String color);
}