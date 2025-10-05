package com.example.manage_revenue_ticket.repository;

import com.example.manage_revenue_ticket.entity.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevenueRepository extends JpaRepository<Revenue,Long> {
}
