package com.example.manage_revenue_ticket.exception;

public class UnauthorizedRoleException extends RuntimeException {
    public UnauthorizedRoleException(String message) {
        super(message);
    }
}
