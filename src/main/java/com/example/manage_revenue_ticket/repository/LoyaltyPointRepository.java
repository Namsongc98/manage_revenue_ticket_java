package com.example.manage_revenue_ticket.repository;

import com.example.manage_revenue_ticket.entity.LoyaltyPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoyaltyPointRepository extends JpaRepository<LoyaltyPoint, Long> {
}
