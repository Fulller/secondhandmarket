package com.secondhandmarket.model;

import com.secondhandmarket.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_request_id", nullable = false)
    private PurchaseRequest purchaseRequest;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime updated_at;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @PrePersist
    protected void onCreate() {
        this.created_at = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated_at = LocalDateTime.now();
    }
}
