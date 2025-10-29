package com.example.manage_revenue_ticket.entity;

import com.example.manage_revenue_ticket.Enum.CustomerStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    // 🔗 Mỗi vé thuộc về 1 chuyến xe
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false, foreignKey = @ForeignKey(name = "tickets_fk_trip"))
    @JsonIgnore
    private Trip trip;

    // 🔗 Người mua vé (customer)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "tickets_fk_customer"))
    @JsonIgnore
    private User customer;

    // 🔗 Nhân viên bán vé (collector)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", foreignKey = @ForeignKey(name = "tickets_fk_seller"))
    @JsonIgnore
    private User seller;

    @Column(name = "seat_number")
    private Integer seatNumber;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private CustomerStatus userStatus;

    @Column(name = "issued_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime issuedAt;

    @PrePersist
    protected void onCreate() {
        this.issuedAt = LocalDateTime.now();
    }
}
