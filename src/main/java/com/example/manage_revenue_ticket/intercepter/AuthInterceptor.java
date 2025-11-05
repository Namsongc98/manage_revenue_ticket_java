package com.example.manage_revenue_ticket.intercepter;

import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.anotation.NoAuth;
import com.example.manage_revenue_ticket.anotation.RoleRequired;
import com.example.manage_revenue_ticket.entity.User;
import com.example.manage_revenue_ticket.exception.UnauthorizedRoleException;
import com.example.manage_revenue_ticket.service.UserService;
import com.example.manage_revenue_ticket.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
//        // Kiểm tra annotation @NoAuth
        NoAuth noAuth = handlerMethod.getMethodAnnotation(NoAuth.class);
        RoleRequired roleRequired = handlerMethod.getMethodAnnotation(RoleRequired.class);
        if (noAuth == null) {
            noAuth = handlerMethod.getBeanType().getAnnotation(NoAuth.class);
        }
        if (noAuth != null) {
            return true; // API public, bỏ qua check
        }
        if(roleRequired == null){
            throw new UnauthorizedRoleException("Bạn không có quyền truy cập tài nguyên này.");
        }
        // Lấy token từ header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);
            System.out.println(jwtUtil.validateToken(jwtToken));
            if (jwtUtil.validateToken(jwtToken)) {
                Long userId = jwtUtil.extractUserId(jwtToken);

                String userRole = jwtUtil.getRoleFromToken(jwtToken);
                UserRole[] allowedRoles = roleRequired.value();
                boolean authorized = Arrays.stream(allowedRoles)
                        .anyMatch(role -> role.name().equalsIgnoreCase(userRole));
                if (authorized) {
                    request.setAttribute("id", userId);
                    request.setAttribute("role", userRole);
                    return true;
                } else {
                    throw new UnauthorizedRoleException("Bạn không có quyền truy cập tài nguyên này.");
                }
            }
        }
        // Token sai hoặc không có → 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(mapper.writeValueAsString(
                BaseResponseDto.error(HttpServletResponse.SC_UNAUTHORIZED,
                        "Unauthorized - Invalid or missing JWT")));
        return false;
    };
}