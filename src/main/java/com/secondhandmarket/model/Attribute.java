package com.secondhandmarket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToMany(mappedBy = "attributes")
    @JsonIgnore
    private Set<Category> categories = new HashSet<>();

    @Override
    public String toString() {
        return "Attribute{id=" + id + ", name='" + name + '\'' + '}';
    }
}
