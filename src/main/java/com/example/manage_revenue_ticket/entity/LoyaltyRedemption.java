package com.example.manage_revenue_ticket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "loyalty_redemption")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyRedemption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "reward_id")
    private LoyaltyReward reward;

    private Integer pointsSpent;
    private LocalDateTime redeemedAt;

    @Enumerated(EnumType.STRING)
    private Status status; // SUCCESS, FAILED, PENDING

    public enum Status {
        SUCCESS, FAILED, PENDING
    }
}
