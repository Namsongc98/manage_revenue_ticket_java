package com.example.manage_revenue_ticket.repository;

import com.example.manage_revenue_ticket.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
}
