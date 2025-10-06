package com.example.manage_revenue_ticket.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import com.example.manage_revenue_ticket.Enum.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final String key;

    public JwtUtil(@Value("${jwt.secret}")String key) {
        this.key = key;
    }


    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    // Tạo Access Token
    public String generateAccessToken(Long idUser, UserRole role) {
        // 15 phút
        long accessTokenExpiration = 1000 * 60 * 15;
        return Jwts.builder()
                .setSubject(String.valueOf(idUser))
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    // Tạo Refresh Token
    public String generateRefreshToken(Long idUser, UserRole role) {
        // 1 ngày
        long refreshTokenExpiration = 1000 * 60 * 60 * 24;
        return Jwts.builder()
                .setSubject(String.valueOf(idUser))
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    // Lấy username từ token
    public Long extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    // 🔹 Lấy role từ token
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }

    // Kiểm tra token hợp lệ
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // 🔹 Kiểm tra token còn hạn không
    public boolean isTokenExpired(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().before(new Date());
    }
}
