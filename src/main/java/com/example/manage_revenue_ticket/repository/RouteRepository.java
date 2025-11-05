package com.example.manage_revenue_ticket.repository;

import com.example.manage_revenue_ticket.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
