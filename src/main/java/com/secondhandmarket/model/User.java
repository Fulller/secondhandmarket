package com.secondhandmarket.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.secondhandmarket.enums.ERole;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String phone;

    private String email;

    private String password;

    private String avatar;

    @Column(nullable = false)
    private boolean isFromOutside = false;

    private String providerName;

    private String providerId;

    private Double rating;

    @ElementCollection(targetClass = ERole.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<ERole> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seller", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Product> products = new HashSet<>();

    public boolean getIsFromOutside() {
        return this.isFromOutside;
    }
}
