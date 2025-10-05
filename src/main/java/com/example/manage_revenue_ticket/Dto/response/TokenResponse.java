package com.example.manage_revenue_ticket.Dto.response;


import lombok.*;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
