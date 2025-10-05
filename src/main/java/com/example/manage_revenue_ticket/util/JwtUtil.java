package com.example.manage_revenue_ticket.util;

import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Tạo Access Token
    public String generateAccessToken(Long idUser) {
        // 15 phút
        long accessTokenExpiration = 1000 * 60 * 15;
        return Jwts.builder()
                .setSubject(String.valueOf(idUser))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(key)
                .compact();
    }

    // Tạo Refresh Token
    public String generateRefreshToken(Long idUser) {
        // 1 ngày
        long refreshTokenExpiration = 1000 * 60 * 60 * 24;
        return Jwts.builder()
                .setSubject(String.valueOf(idUser))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(key)
                .compact();
    }

    // Lấy username từ token
    public Long extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    // Kiểm tra token hợp lệ
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
