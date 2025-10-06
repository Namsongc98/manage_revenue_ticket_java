package com.example.manage_revenue_ticket.repository;

import com.example.manage_revenue_ticket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long > {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String Email);
}
