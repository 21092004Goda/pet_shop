package org.kuro.cat_microservice.service.mapper;

import org.kuro.entity.Cat;
import org.kuro.cat_microservice.repository.CatRepository;
import org.kuro.cat_microservice.repository.OwnerRepository;
import org.kuro.entity.dto.CatDto;

import java.util.Optional;
import java.util.stream.Collectors;

public class CatMapper {
    public static CatDto convertCatToCatDto(Cat cat) {
        CatDto catDto = new CatDto();
        catDto.setId(cat.getId());
        catDto.setName(cat.getName());
        catDto.setBreed(cat.getBreed());
        catDto.setColor(cat.getColor());
        catDto.setOwner(cat.getOwner().getId());
        catDto.setFriends(cat.getFriends().stream()
                .map(Cat::getId)
                .collect(Collectors.toList()));
        return catDto;
    }

    public static Cat convertCatDtoToCat(CatDto catDto, OwnerRepository ownerRepository, CatRepository catRepository) {
        Cat cat = new Cat();
        cat.setId(catDto.getId());
        cat.setName(catDto.getName());
        cat.setBreed(catDto.getBreed());
        cat.setColor(catDto.getColor());
        cat.setOwner(ownerRepository.findById(catDto.getOwner()).orElseThrow(() -> new IllegalArgumentException("Owner not found")));
        cat.setFriends(catDto.getFriends().stream()
                .map(catRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList()));
        return cat;
    }
}