package com.example.manage_revenue_ticket.repository;

import com.example.manage_revenue_ticket.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
    List<Salary> findByPeriodMonthAndPeriodYearAndUserId(byte month, short year, Long userId);
}
