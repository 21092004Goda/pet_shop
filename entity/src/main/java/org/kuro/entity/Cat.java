package org.kuro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter

@Entity
@Table(name = "cat", schema = "cat_observer")
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "breed", length = 100)
    private String breed;

    @Column(name = "color", length = 100)
    private String color;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cat_friend",
            joinColumns = @JoinColumn(name = "id_cat"),
            inverseJoinColumns = @JoinColumn(name = "id_friend")
    )
    private List<Cat> friends;
}