package com.example.manage_revenue_ticket.service;

import com.example.manage_revenue_ticket.Dto.request.UserRequestDto;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.entity.User;
import com.example.manage_revenue_ticket.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class AuthService {
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public User register(@Valid UserRequestDto user) {

        String email = user.getEmail();

        if(userRepository.existsByEmail(email)){
            throw new IllegalArgumentException("Email already exists");
        }

        String password = passwordEncoder.encode(user.getPassword());
        UserRole role = (user.getRole() == null) ? UserRole.CUSTOMER : user.getRole();

        try{
            User newUser = User.builder()
                    .email(email)
                    .password(password)
                    .role(role)
                    .createdAt(LocalDateTime.now())
                    .build();
            return userRepository.save(newUser);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

//    public User updateWord(@Valid UserRequestDto user){
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với email: " + email));
//
//        // Kiểm tra xem user đã có mật khẩu chưa
//        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
//            throw new RuntimeException("Tài khoản đã có mật khẩu, không thể đặt lại theo cách này!");
//        }
//
//        // Mã hoá mật khẩu mới
//        user.setPassword(passwordEncoder.encode(newPassword));
//
//        userRepository.save(user);
//        return "Đặt mật khẩu thành công cho tài khoản: " + user.getEmail();
//    }
}
