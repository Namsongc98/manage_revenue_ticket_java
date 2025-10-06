package com.example.manage_revenue_ticket.service;

import com.example.manage_revenue_ticket.Dto.request.UserRequestDto;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.entity.User;
import com.example.manage_revenue_ticket.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    public void updatePassWord(@Valid UserRequestDto userRequest, String oldPassword){
        String email = userRequest.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với email: " + email));

        // Kiểm tra xem user đã có mật khẩu chưa
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }
        System.out.println(userRequest.getPassword());
        String newPassword = passwordEncoder.encode(userRequest.getPassword());
        // Mã hoá mật khẩu mới
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public User login(@Valid UserRequestDto user){
        String email = user.getEmail();
        String password = user.getPassword();

        User checkUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(password, checkUser.getPassword())){
            return checkUser;
        }else {
            throw new RuntimeException("Invalid email or password");
        }
    }
}
