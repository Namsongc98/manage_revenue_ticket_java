package com.example.manage_revenue_ticket.controller;

import com.example.manage_revenue_ticket.Dto.request.BaseLoyaltyPointsRequest;
import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.anotation.RoleRequired;
import com.example.manage_revenue_ticket.entity.BaseLoyaltyPoints;
import com.example.manage_revenue_ticket.service.BaseLoyaltyPointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/base_loyalty_point")
public class BaseLoyaltyPointsController {

    @Autowired
    private  BaseLoyaltyPointsService baseLoyaltyPointsService;

    // 🆕 Tạo mới chính sách tích điểm
    @PostMapping
    @RoleRequired({UserRole.ADMIN,UserRole.EMPLOYEE})
    public ResponseEntity<BaseResponseDto<BaseLoyaltyPoints>> create(@RequestBody BaseLoyaltyPointsRequest request) {
        System.out.println("RequestBody"+request);
        BaseLoyaltyPoints created = baseLoyaltyPointsService.create(request);
        return ResponseEntity.ok(BaseResponseDto.success(201, "Tạo thành công",created));
    }

    // ✏️ Cập nhật chính sách tích điểm
    @PutMapping("/{id}")
    @RoleRequired({UserRole.ADMIN,UserRole.EMPLOYEE})
    public ResponseEntity<BaseResponseDto<BaseLoyaltyPoints>> update(
            @PathVariable Long id,
            @RequestBody BaseLoyaltyPointsRequest request
    ) {
        BaseLoyaltyPoints updated = baseLoyaltyPointsService.update(id, request);
        return ResponseEntity.ok(BaseResponseDto.success(201, "Tạo thành công",updated));
    }
}
