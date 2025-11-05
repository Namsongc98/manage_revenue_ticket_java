package com.example.manage_revenue_ticket.Dto.request;

import com.example.manage_revenue_ticket.Enum.RouteStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class RouteRequestDto {
    private String routeName;
    private String startPoint;
    private String endPoint;
    private BigDecimal distanceKm;
    private RouteStatus status = RouteStatus.ACTIVE; // Mặc định ACTIVE
}
