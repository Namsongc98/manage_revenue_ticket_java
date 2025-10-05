package com.example.manage_revenue_ticket.repository;

import com.example.manage_revenue_ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
