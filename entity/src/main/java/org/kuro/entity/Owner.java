package org.kuro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter

@Entity
@Table(name = "owner", schema = "cat_observer")
public class Owner {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "role", length = 100)
    private String role;

    @Column(name = "password", length = 100)
    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Cat> cats;
}