package org.kuro.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CatDto {
    private UUID id;
    private String name;
    private String breed;
    private String color;
    private UUID owner;
    private List<UUID> friends;
}