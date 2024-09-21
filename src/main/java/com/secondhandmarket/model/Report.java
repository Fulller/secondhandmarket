package com.secondhandmarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defendant_id", nullable = false)
    private User defendant;

    @ManyToOne
    @JoinColumn(name = "accused_id", nullable = false)
    private User accused;

    @Column(nullable = false)
    private String reason;

    private String phone;

    private String email;
}
