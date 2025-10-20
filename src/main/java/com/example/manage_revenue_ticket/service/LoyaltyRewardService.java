package com.example.manage_revenue_ticket.service;

import com.example.manage_revenue_ticket.entity.LoyaltyReward;
import com.example.manage_revenue_ticket.repository.LoyaltyRewardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoyaltyRewardService {

    private LoyaltyRewardRepository rewardRepo;



    public LoyaltyReward create(LoyaltyReward payload) {
        payload.setCreatedAt(LocalDateTime.now());
        payload.setUpdatedAt(LocalDateTime.now());
        // ensure status/active consistency
        if (payload.getStatus() == null) payload.setStatus(LoyaltyReward.Status.ACTIVE);
        if (payload.getActive() == null) payload.setActive(Boolean.TRUE);
        return rewardRepo.save(payload);
    }

    public LoyaltyReward update(Long id, LoyaltyReward payload) {
        LoyaltyReward existing = rewardRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found: " + id));
        existing.setRewardName(payload.getRewardName());
        existing.setDescription(payload.getDescription());
        existing.setPointsRequired(payload.getPointsRequired());
        existing.setRewardType(payload.getRewardType());
        existing.setActive(payload.getActive());
        existing.setStatus(payload.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());
        return rewardRepo.save(existing);
    }
}
