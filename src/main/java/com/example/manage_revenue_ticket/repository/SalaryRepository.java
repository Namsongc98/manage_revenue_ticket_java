package com.example.manage_revenue_ticket.repository;

import com.example.manage_revenue_ticket.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
}
