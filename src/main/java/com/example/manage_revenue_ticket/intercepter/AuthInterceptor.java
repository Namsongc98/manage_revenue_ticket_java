package com.example.manage_revenue_ticket.intercepter;

import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.entity.User;
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
//        NoAuth noAuth = handlerMethod.getMethodAnnotation(NoAuth.class);
//        AdminOnly adminOnly = handlerMethod.getMethodAnnotation(AdminOnly.class);
//        CustomerOnly customerOnly = handlerMethod.getMethodAnnotation(CustomerOnly.class);
//        StaffOnly staffOnly = handlerMethod.getMethodAnnotation(StaffOnly.class);

//        RoleRequired roleRequired = handlerMethod.getMethodAnnotation(RoleRequired.class);

//        if (noAuth == null) {
//            noAuth = handlerMethod.getBeanType().getAnnotation(NoAuth.class);
//        }
//        if (adminOnly == null) {
//            adminOnly = handlerMethod.getBeanType().getAnnotation(AdminOnly.class);
//        }
//        if (customerOnly == null) {
//            customerOnly = handlerMethod.getBeanType().getAnnotation(CustomerOnly.class);
//        }
//        if (staffOnly == null) {
//            staffOnly = handlerMethod.getBeanType().getAnnotation(StaffOnly.class);
//        }

//        if (noAuth != null) {
//            return true; // API public, bỏ qua check
//        }

        String path = request.getRequestURI();

        // Ví dụ: bỏ qua auth cho ảnh avatar
        if (path.startsWith("/avatars/")) {
            return true; // cho phép request đi tiếp
        }

        // Lấy token từ header
        String authHeader = request.getHeader("Authorization");
        System.out.println(authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);
            System.out.println(jwtUtil.validateToken(jwtToken));
            if (jwtUtil.validateToken(jwtToken)) {
                Long userId = jwtUtil.extractUserId(jwtToken);
                User user = userService.getUserById(userId);
                UserRole userRole = user.getRole();
//                boolean authorized =
//                        (adminOnly != null && userRole == Role.ADMIN)
//                                || (customerOnly != null && userRole == Role.CUSTOMER)
//                                || (staffOnly != null && userRole == Role.STAFF)
//                                ||  Arrays.asList(roleRequired.value()).contains(userRole);

//                if (authorized) {
//                    request.setAttribute("id", userId);
//                    return true;
//                } else {
//                    return false;
//                }
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