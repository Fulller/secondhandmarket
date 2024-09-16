package com.secondhandmarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "attributes")
    private Set<CategoryChild> categoryChilds = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attribute", orphanRemoval = true)
    private List<ProductAttribute> productAttributes = new ArrayList<>();
}
