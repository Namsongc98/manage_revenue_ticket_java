package com.example.manage_revenue_ticket.service;

import com.example.manage_revenue_ticket.Dto.request.LoyaltyRedemptionRequest;
import com.example.manage_revenue_ticket.entity.LoyaltyPoint;
import com.example.manage_revenue_ticket.entity.LoyaltyRedemption;
import com.example.manage_revenue_ticket.entity.LoyaltyReward;
import com.example.manage_revenue_ticket.entity.User;
import com.example.manage_revenue_ticket.repository.LoyaltyPointRepository;
import com.example.manage_revenue_ticket.repository.LoyaltyRedemptionRepository;
import com.example.manage_revenue_ticket.repository.LoyaltyRewardRepository;
import com.example.manage_revenue_ticket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LoyaltyRedemptionService {

    @Autowired
    private  LoyaltyRewardRepository rewardRepo;

    @Autowired
    private  LoyaltyPointRepository pointRepo;

    @Autowired
    UserRepository usersRepository;


    @Autowired
    private  LoyaltyRedemptionRepository redemptionRepo;

    @Transactional
    public LoyaltyRedemption redeemReward(Long customerId, Long rewardId) {
        LoyaltyReward reward = rewardRepo.findById(rewardId)
                .orElseThrow(() -> new RuntimeException("Reward not found"));

        LoyaltyPoint loyaltyPoint = pointRepo.findFirstByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (loyaltyPoint.getPoints() < reward.getRequiredPoints()) {
            throw new RuntimeException("Not enough points to redeem this reward");
        }

        // Trừ điểm
        loyaltyPoint.setPoints(loyaltyPoint.getPoints() - reward.getRequiredPoints());
        pointRepo.save(loyaltyPoint);

        // Lưu lịch sử đổi điểm
        LoyaltyRedemption redemption = LoyaltyRedemption.builder()
                .customer(loyaltyPoint.getCustomer())
                .reward(reward)
                .pointsSpent(reward.getRequiredPoints())
                .redeemedAt(LocalDateTime.now())
                .status(LoyaltyRedemption.Status.SUCCESS)
                .build();

        return redemptionRepo.save(redemption);
    }

    @Transactional
    public LoyaltyRedemption create(Long customerId, Long rewardId) {
        User customer = usersRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));

        LoyaltyReward reward = rewardRepo.findById(rewardId)
                .orElseThrow(() -> new RuntimeException("Reward not found: " + rewardId));

        LoyaltyRedemption redemption = new LoyaltyRedemption();
        redemption.setCustomer(customer);
        redemption.setReward(reward);
        redemption.setPointsSpent(reward.getPointsRequired());
        redemption.setRedeemedAt(LocalDateTime.now());
        redemption.setStatus(LoyaltyRedemption.Status.PENDING);

        return redemptionRepo.save(redemption);
    }

    /**
     * Update status of an existing redemption.
     * If changing to SUCCESS -> will attempt to deduct points from customer's LoyaltyPoint.
     */
    @Transactional
    public LoyaltyRedemption updateStatus(Long redemptionId, LoyaltyRedemption.Status newStatus) {
        LoyaltyRedemption redemption = redemptionRepo.findById(redemptionId)
                .orElseThrow(() -> new RuntimeException("Redemption not found: " + redemptionId));

        LoyaltyRedemption.Status old = redemption.getStatus();
        redemption.setStatus(newStatus);

        if (old != LoyaltyRedemption.Status.SUCCESS && newStatus == LoyaltyRedemption.Status.SUCCESS) {
            // perform points deduction
            Long customerId = redemption.getCustomer().getId();
            LoyaltyPoint lp = pointRepo.findFirstByCustomerId(customerId)
                    .orElseThrow(() -> new RuntimeException("No loyalty point record for customer: " + customerId));

            int left = lp.getPoints() - redemption.getPointsSpent();
            if (left < 0) {
                throw new RuntimeException("Not enough points to complete redemption");
            }
            lp.setPoints(left);
            pointRepo.save(lp);
        }

        return redemptionRepo.save(redemption);
    }
}
