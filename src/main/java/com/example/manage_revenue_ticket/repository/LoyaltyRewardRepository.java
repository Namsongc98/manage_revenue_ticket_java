package com.example.manage_revenue_ticket.repository;

import com.example.manage_revenue_ticket.entity.LoyaltyReward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoyaltyRewardRepository extends JpaRepository<LoyaltyReward, Long> {
    List<LoyaltyReward> findByActiveTrue();
}
