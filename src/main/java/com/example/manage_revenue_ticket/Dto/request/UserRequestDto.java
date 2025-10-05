package com.example.manage_revenue_ticket.Dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.example.manage_revenue_ticket.Enum.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    private UserRole role;     // optional

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;  // required

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password; // required
}