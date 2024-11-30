package org.kuro.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OwnerDto {
    private UUID id;
    private String name;
    private Date birthday;
    private String role;
    private String password;
    private List<UUID> cats;
}