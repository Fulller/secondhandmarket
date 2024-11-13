package com.secondhandmarket.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    @JsonBackReference
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attribute", orphanRemoval = true)
    @JsonManagedReference
    private List<Option> options = new ArrayList<>();

    private Boolean isRequired = false;

    private Boolean isEnter = false;
}
