package com.secondhandmarket.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private LocalDateTime reportedAt;
}
