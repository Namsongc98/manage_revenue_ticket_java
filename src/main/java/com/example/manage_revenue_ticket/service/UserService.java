package com.example.manage_revenue_ticket.service;

import com.example.manage_revenue_ticket.entity.User;
import com.example.manage_revenue_ticket.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;


    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

}
