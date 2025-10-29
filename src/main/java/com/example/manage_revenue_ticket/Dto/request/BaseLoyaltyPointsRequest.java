package com.example.manage_revenue_ticket.Dto.request;


import com.example.manage_revenue_ticket.Enum.BaseLoyaltyPointStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BaseLoyaltyPointsRequest {

    private String roleName;

    private int ticketsPerPoint;

    private BigDecimal pointValue;

    private Integer maxPointsPerMonth;

    private LocalDate startDate;

    private LocalDate endDate;

    private BaseLoyaltyPointStatus status;
}
