package com.example.manage_revenue_ticket.service;

import com.example.manage_revenue_ticket.repository.BusesRepository;
import com.example.manage_revenue_ticket.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaryService {
    @Autowired
    private SalaryRepository salaryRepository;
}
