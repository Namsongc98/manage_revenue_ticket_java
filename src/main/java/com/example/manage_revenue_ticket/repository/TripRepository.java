package com.example.manage_revenue_ticket.repository;

import com.example.manage_revenue_ticket.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    @Query(value = """
        SELECT\s
            b.id AS busId,\s
            b.plate_number AS bienSoXe,\s
            b.capacity AS dungTich,\s
            b.status AS busStatus,\s
            t.revenue AS doanhThu,\s
            t.departure_time AS khoiHanhTime,\s
            t.arrival_time AS denTime,
            r.route_name AS tenChuyen,\s
            r.start_point AS choKhoiHang,\s
            r.end_point AS choDen,\s
            r.distance_km AS km
        FROM buses b
        JOIN trips t ON t.bus_id = b.id
        JOIN routes r ON t.route_id = r.id
        WHERE r.status = 'ACTIVE'\s
          AND t.status = 'SCHEDULED'
          AND b.status = 'PENDING'
   \s""", nativeQuery = true)
    List<Object[]> findTripBusRouteInfo();
    @Query("""
        SELECT t.driver.id, COUNT(t.id) as total
        FROM Trip t
        WHERE t.status = 'COMPLETED'
          AND MONTH(t.arrivalTime) = :month
          AND YEAR(t.arrivalTime) = :year
          AND (:userId IS NULL OR t.driver.id = :userId)
        GROUP BY t.driver.id
    """)
    List<Object[]> countCompletedTripsByDriver(@Param("month") byte month, @Param("year") short year,@Param("userId") Long userId);

    @Query("""
        SELECT COUNT(t.id) as total
        FROM Trip t
        WHERE t.status = 'COMPLETED'
          AND MONTH(t.arrivalTime) = :month
          AND YEAR(t.arrivalTime) = :year
          AND (:userId IS NULL OR t.driver.id = :userId)
    """)
    Long countCompletedTripsByDriverForOne(@Param("month") byte month, @Param("year") short year,@Param("userId") Long userId);
}
