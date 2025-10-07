package com.example.manage_revenue_ticket.service;

import com.example.manage_revenue_ticket.repository.RevenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RevenueService {
    @Autowired
    public RevenueRepository revenueRepository;
}
