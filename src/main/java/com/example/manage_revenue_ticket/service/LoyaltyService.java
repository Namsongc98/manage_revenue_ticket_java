package com.example.manage_revenue_ticket.service;

import com.example.manage_revenue_ticket.repository.LoyaltyPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoyaltyService {
    @Autowired
    private LoyaltyPointRepository loyaltyPointRepository;

}
