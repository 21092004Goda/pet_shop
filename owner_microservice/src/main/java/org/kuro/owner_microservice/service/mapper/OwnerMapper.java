package org.kuro.owner_microservice.service.mapper;

import org.kuro.entity.Cat;
import org.kuro.entity.Owner;
import org.kuro.entity.dto.OwnerDto;
import org.kuro.owner_microservice.repository.CatRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.Collectors;

public class OwnerMapper {
    public static OwnerDto convertToDTO(Owner owner) {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setId(owner.getId());
        ownerDto.setName(owner.getName());
        ownerDto.setBirthday(owner.getDateOfBirth());
        ownerDto.setRole(owner.getRole());
        ownerDto.setPassword(owner.getPassword());
        ownerDto.setCats(owner.getCats().stream()
                .map(Cat::getId)
                .collect(Collectors.toList()));
        return ownerDto;
    }

    public static Owner convertToEntity(OwnerDto ownerDto, CatRepository catRepository) {
        Owner owner = new Owner();
        owner.setId(ownerDto.getId());
        owner.setName(ownerDto.getName());
        owner.setDateOfBirth(ownerDto.getBirthday());
        owner.setRole(ownerDto.getRole());

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(ownerDto.getPassword());
        owner.setPassword(encodedPassword);

        owner.setCats(ownerDto.getCats().stream()
                .map(catRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList()));
        return owner;
    }


}