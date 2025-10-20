package com.example.manage_revenue_ticket.controller;

import com.example.manage_revenue_ticket.Dto.request.LoyaltyRedemptionRequest;
import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.anotation.RoleRequired;
import com.example.manage_revenue_ticket.entity.LoyaltyRedemption;
import com.example.manage_revenue_ticket.entity.LoyaltyReward;
import com.example.manage_revenue_ticket.repository.LoyaltyRewardRepository;
import com.example.manage_revenue_ticket.service.LoyaltyRedemptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class LoyaltyRedemptionController {

    @Autowired
    private LoyaltyRewardRepository rewardRepo;

    @Autowired
    private LoyaltyRedemptionService redemptionService;

    @GetMapping("/rewards")
    public List<LoyaltyReward> getAllActiveRewards() {
        return rewardRepo.findByActiveTrue();
    }

    @PostMapping("/redeem/{rewardId}")
    public ResponseEntity<?> redeemReward(@PathVariable Long rewardId,
                                          @RequestParam Long customerId) {
        try {
            LoyaltyRedemption redemption = redemptionService.redeemReward(customerId, rewardId);
            return ResponseEntity.ok(BaseResponseDto.success(200,"create thành công",redemption));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponseDto.error(400, e.getMessage()));
        }
    }

    @PostMapping
    @RoleRequired(UserRole.ADMIN)
    public ResponseEntity<LoyaltyRedemption> create(@RequestBody LoyaltyRedemptionRequest req) {
        LoyaltyRedemption created = redemptionService.create(req.getCustomerId(), req.getRewardId());
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}/status")
    @RoleRequired(UserRole.ADMIN)
    public ResponseEntity<LoyaltyRedemption> updateStatus(@PathVariable Long id, @RequestBody LoyaltyRedemptionRequest req) {
        LoyaltyRedemption.Status status = LoyaltyRedemption.Status.valueOf(req.getStatus().name());
        LoyaltyRedemption updated = redemptionService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}
