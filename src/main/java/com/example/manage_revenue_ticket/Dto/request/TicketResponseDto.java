package com.example.manage_revenue_ticket.Dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TicketResponseDto {

    private Long tripId;
    private Long sellerId;
    private Long customerId;
    private Integer seatNumber;
    private BigDecimal price;
    private LocalDateTime issuedAt;
}
