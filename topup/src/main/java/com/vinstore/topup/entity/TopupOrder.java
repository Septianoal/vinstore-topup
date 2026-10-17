package com.vinstore.topup.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class TopupOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_code", nullable = false, unique = true, length = 30)
    private String orderCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "game_user_id", nullable = false, length = 100)
    private String gameUserId;

    @Column(nullable = false, length = 100)
    private String contact;

    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected TopupOrder() {
    }

    public TopupOrder(String orderCode, Product product, String gameUserId, String contact) {
        this.orderCode = orderCode;
        this.product = product;
        this.gameUserId = gameUserId;
        this.contact = contact;
        this.totalPrice = product.getPrice();
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getOrderCode() { return orderCode; }
    public Product getProduct() { return product; }
    public String getGameUserId() { return gameUserId; }
    public String getContact() { return contact; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}