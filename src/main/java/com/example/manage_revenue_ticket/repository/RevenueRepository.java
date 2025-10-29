package com.example.manage_revenue_ticket.repository;

import com.example.manage_revenue_ticket.entity.Revenue;
import com.example.manage_revenue_ticket.projection.TopUserProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RevenueRepository extends JpaRepository<Revenue,Long> {
    // Doanh thu của 1 bus theo ngày
    @Query("""
        SELECT SUM(r.totalAmount)
        FROM Revenue r
        WHERE r.trip.bus.id = :busId
          AND r.reportDate = :date
    """)
    BigDecimal getRevenueByBusAndDate(@Param("busId") Long busId, @Param("date") LocalDate date);

    // Doanh thu của 1 bus trong khoảng ngày
    @Query("""
        SELECT SUM(r.totalAmount)
        FROM Revenue r
        WHERE r.trip.bus.id = :busId
          AND r.reportDate BETWEEN :startDate AND :endDate
    """)
    BigDecimal getRevenueByBusAndRange(@Param("busId") Long busId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);

    // Doanh thu tổng hợp tất cả bus trong ngày
    @Query("""
        SELECT r.trip.bus.id, r.trip.bus.plateNumber, SUM(r.totalAmount)
        FROM Revenue r
        WHERE r.reportDate = :date
        GROUP BY r.trip.bus.id, r.trip.bus.plateNumber
    """)
    List<Object[]> getTotalRevenueByAllBusesInDate(@Param("date") LocalDate date);

    @Query("""
        SELECT 
            u.id AS userId,
            u.email AS email,
            COUNT(t.id) AS totalTickets,
            SUM(t.price) AS totalAmount
        FROM Ticket t
        JOIN t.seller u
        WHERE u.role = 'EMPLOYEE'
        		AND YEAR(t.issuedAt) = :year
                AND (:month IS NULL OR MONTH(t.issuedAt) = :month)
                AND (:day IS NULL OR DAY(t.issuedAt) = :day)
        GROUP BY u.id, u.email
        ORDER BY COUNT(t.id) DESC
    """)
    List<TopUserProjection> findTopEmployees(
            Pageable pageable,
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("day") Integer day);

    @Query("""
    SELECT 
        u.id AS userId,
        u.email AS email,
        COUNT(t.id) AS totalTickets,
        SUM(t.price) AS totalAmount
    FROM Ticket t
    JOIN t.customer u
    WHERE u.role = 'CUSTOMER'
    	AND YEAR(t.issuedAt) = :year
        AND (:month IS NULL OR MONTH(t.issuedAt) = :month)
        AND (:day IS NULL OR DAY(t.issuedAt) = :day)\s
    GROUP BY u.id, u.email
    ORDER BY SUM(t.price) DESC
""")
    List<TopUserProjection> findTopCustomers(
            Pageable pageable,
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("day") Integer day);
}
