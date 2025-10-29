package com.example.manage_revenue_ticket.service;

import com.example.manage_revenue_ticket.entity.Salary;
import com.example.manage_revenue_ticket.projection.CustomerRevenueByRouteProjection;
import com.example.manage_revenue_ticket.projection.TopUserProjection;
import com.example.manage_revenue_ticket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RevenueService {
    @Autowired
    public RevenueRepository revenueRepository;

    @Autowired
    public BusesRepository busesRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public TicketRepository ticketRepository;

    @Autowired
    public SalaryRepository salaryRepository;

    public List<Map<String, Object>> getRevenueByBusAndDate(Long busId, LocalDate date) {
        int year = date.getYear();     // Lấy năm
        int month = date.getMonthValue(); // Lấy tháng (1–12)
        int day = date.getDayOfMonth();   // Lấy ngày (1–31)
        return busesRepository.findBusRevenueNative(year, month, day, busId);
    }

    public List<Map<String, Object>> getRevenueByEmployeeAndDate(Long userId, LocalDate date) {
        int year = date.getYear();     // Lấy năm
        int month = date.getMonthValue(); // Lấy tháng (1–12)
        int day = date.getDayOfMonth();   // Lấy ngày (1–31)
        return busesRepository.findEmployeeRevenueNative(year, month, day, userId);
    }

    public List<CustomerRevenueByRouteProjection> getRevenueByUserAndDate(Long userId, Integer date, Integer month, Integer year) {
        return   busesRepository.getCustomerRevenueByRouteAndDate(year, month, date, userId);
    }

    public BigDecimal getRevenueByBusAndRange(Long busId, LocalDate start, LocalDate end) {
        return Optional.ofNullable(revenueRepository.getRevenueByBusAndRange(busId, start, end))
                .orElse(BigDecimal.ZERO);
    }

    public List<Map<String, Object>> getRevenueAllBusesInDate(LocalDate date) {
        List<Object[]> result = revenueRepository.getTotalRevenueByAllBusesInDate(date);

        return result.stream().map(row -> Map.of(
                "busId", row[0],
                "plateNumber", row[1],
                "totalRevenue", row[2]
        )).toList();
    }

    public List<TopUserProjection> getTopEmployees(int limit,Integer year, Integer month, Integer day) {
        return revenueRepository.findTopEmployees(PageRequest.of(0, limit),year,month,day);
    }

    public List<TopUserProjection> getTopCustomers(int limit,Integer year, Integer month, Integer day) {
        return revenueRepository.findTopCustomers(PageRequest.of(0, limit),year,month,day);
    }
}
