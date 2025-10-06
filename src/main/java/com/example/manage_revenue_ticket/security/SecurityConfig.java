package com.example.manage_revenue_ticket.security;

import com.example.manage_revenue_ticket.exception.CustomAccessDeniedHandler;
import com.example.manage_revenue_ticket.exception.CustomAuthEntryPoint;
import com.example.manage_revenue_ticket.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomAuthEntryPoint authEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          CustomAuthEntryPoint authEntryPoint,
                          CustomAccessDeniedHandler accessDeniedHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authEntryPoint = authEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // tắt CSRF cho REST API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // login, register không cần token
//                        .requestMatchers("/**").permitAll()
//                        .requestMatchers("/avatars/**").permitAll()
//                        .requestMatchers("/favicon.ico").permitAll()
                        .anyRequest().authenticated()                // các API khác cần JWT
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authEntryPoint)   // lỗi 401
                        .accessDeniedHandler(accessDeniedHandler)   // lỗi 403
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
