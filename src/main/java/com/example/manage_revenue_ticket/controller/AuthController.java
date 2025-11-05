package com.example.manage_revenue_ticket.controller;

import com.example.manage_revenue_ticket.Dto.request.UserRequestDto;
import com.example.manage_revenue_ticket.Dto.request.UserUpdatePasswordRequestDto;
import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import com.example.manage_revenue_ticket.Dto.response.TokenResponse;
import com.example.manage_revenue_ticket.anotation.NoAuth;
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

    @PostMapping("/register")
    @NoAuth
    public ResponseEntity<BaseResponseDto<TokenResponse>> register(@Valid @RequestBody UserRequestDto req){
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setRole(req.getRole());
        User savedUser = authService.register(req);
        String accessToken = jwtUtil.generateAccessToken(savedUser.getId(),savedUser.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(savedUser.getId(),savedUser.getRole());
        TokenResponse token = new TokenResponse(accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponseDto.success(201, "Register successfully", token));
    }
    @PutMapping("/update-password")
    @NoAuth
    public ResponseEntity<BaseResponseDto<TokenResponse>> updatePass(@RequestBody UserUpdatePasswordRequestDto req){
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        String oldPassword = req.getOldPassword();
        authService.updatePassWord(req, oldPassword);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponseDto.success(201, "Update Password successfully",null));
    }

    @PostMapping("/login")
    @NoAuth
    public ResponseEntity<BaseResponseDto<TokenResponse>> login(@RequestBody UserRequestDto req){
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setRole(req.getRole());
        User loginUser = authService.login(req);
        String accessToken = jwtUtil.generateAccessToken(loginUser.getId(),loginUser.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(loginUser.getId(),loginUser.getRole());
        TokenResponse token = new TokenResponse(accessToken, refreshToken);
        return ResponseEntity.ok(BaseResponseDto.success(200,"Login successfully",token));
    }
}
