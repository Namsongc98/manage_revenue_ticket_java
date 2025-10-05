package com.example.manage_revenue_ticket.controller;

import com.example.manage_revenue_ticket.Dto.request.UserRequestDto;
import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import com.example.manage_revenue_ticket.Dto.response.TokenResponse;
import com.example.manage_revenue_ticket.entity.User;
import com.example.manage_revenue_ticket.service.AuthService;
import com.example.manage_revenue_ticket.service.UserService;
import com.example.manage_revenue_ticket.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<BaseResponseDto<TokenResponse>> register(@Valid @RequestBody UserRequestDto req){
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setRole(req.getRole());
        User savedUser = authService.register(req);
        String accessToken = jwtUtil.generateAccessToken(savedUser.getId());
        String refreshToken = jwtUtil.generateRefreshToken(savedUser.getId());
        TokenResponse token = new TokenResponse(accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponseDto.success(201, "Register successfully", token));
    }
    @PutMapping
    public ResponseEntity<BaseResponseDto<TokenResponse>> updatePass(@Valid @RequestBody UserRequestDto req){
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setRole(req.getRole());
        User savedUser = authService.register(req);
        String accessToken = jwtUtil.generateAccessToken(savedUser.getId());
        String refreshToken = jwtUtil.generateRefreshToken(savedUser.getId());
        TokenResponse token = new TokenResponse(accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponseDto.success(201, "Register successfully", token));
    }
}
