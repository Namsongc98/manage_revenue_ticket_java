package com.example.manage_revenue_ticket.repository;

import com.example.manage_revenue_ticket.entity.Buses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusesRepository extends JpaRepository<Buses, Long> {
    boolean existsByPlateNumber(String plateNumber);
}
