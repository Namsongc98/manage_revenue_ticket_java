package com.example.manage_revenue_ticket.Dto.request;

import com.example.manage_revenue_ticket.Enum.TransactionType;
import lombok.Data;

@Data
public class LoyaltyPointsRequest {
    private Long customerId;
    private int points;
    private TransactionType transactionType;
    private String description;
}
