package com.example.manage_revenue_ticket.exception;

import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class GlobalExceptionHandler  {

    @ExceptionHandler(UnauthorizedRoleException.class)
    public ResponseEntity<BaseResponseDto<String>> handleUnauthorizedRoleException(UnauthorizedRoleException ex) {
        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        System.out.println("ExceptionHandler"+message);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(BaseResponseDto.error(HttpStatus.FORBIDDEN.value(), message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseDto<String>> handleAllExceptions(Exception ex) {
        // Nếu exception bị bọc (NestedServletException), lấy message gốc
        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponseDto.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), message));
    }

    // 💥 500: Lỗi hệ thống
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponseDto<String>> handleRuntimeException(RuntimeException ex) {
        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponseDto.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), message));
    }

    // ❌ 404: Không tìm thấy tài nguyên
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<BaseResponseDto<String>>handleNotFound(NoSuchElementException ex){
        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.error(HttpStatus.NOT_FOUND.value(), message));
    }
    // ⚠️ 400: Dữ liệu không hợp lệ
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponseDto<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        return ResponseEntity
                .badRequest()
                .body(BaseResponseDto.error(HttpStatus.BAD_REQUEST.value(), message));
    }
    // 🚫 403: Không có quyền
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponseDto<String>> handleAccessDeniedException(AccessDeniedException ex) {
        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(BaseResponseDto.error(HttpStatus.FORBIDDEN.value(), "Access Denied: " + message));
    }

    // 🔒 401: Chưa đăng nhập
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<BaseResponseDto<String>> handleSecurityException(SecurityException ex) {
        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponseDto.error(HttpStatus.UNAUTHORIZED.value(), message));
    }

}
