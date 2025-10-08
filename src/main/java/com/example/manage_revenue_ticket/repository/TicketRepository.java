package com.example.manage_revenue_ticket.repository;

import com.example.manage_revenue_ticket.entity.Ticket;
import com.example.manage_revenue_ticket.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
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
          AND t.id = :tripId
   \s""", nativeQuery = true)
    List<Object[]> findTripBusRouteInfo(@Param("tripId") Long tripId);
}
