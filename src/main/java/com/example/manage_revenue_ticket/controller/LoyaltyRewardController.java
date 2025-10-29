package com.example.manage_revenue_ticket.controller;

import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.anotation.RoleRequired;
import com.example.manage_revenue_ticket.entity.LoyaltyReward;
import com.example.manage_revenue_ticket.service.LoyaltyRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loyalty/rewards")
public class LoyaltyRewardController {

    @Autowired
    private LoyaltyRewardService rewardService;

    @PostMapping
    @RoleRequired(UserRole.ADMIN)
    public ResponseEntity<BaseResponseDto<LoyaltyReward>> create(@RequestBody LoyaltyReward request) {
        LoyaltyReward created = rewardService.create(request);
        return ResponseEntity.ok(BaseResponseDto.success(201,"Cteate succes", created));
    }

    @PutMapping("/{id}")
    @RoleRequired(UserRole.ADMIN)
    public ResponseEntity<BaseResponseDto<LoyaltyReward>> update(@PathVariable Long id, @RequestBody LoyaltyReward request) {
        LoyaltyReward updated = rewardService.update(id, request);
        return ResponseEntity.ok(BaseResponseDto.success(200, "Update succes",updated));
    }
}
