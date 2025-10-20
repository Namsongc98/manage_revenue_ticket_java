package com.example.manage_revenue_ticket.repository;

import com.example.manage_revenue_ticket.entity.LoyaltyRedemption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoyaltyRedemptionRepository extends JpaRepository<LoyaltyRedemption, Long> {
    List<LoyaltyRedemption> findByCustomerId(Long customerId);
}
