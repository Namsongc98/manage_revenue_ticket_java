package com.example.manage_revenue_ticket.service;

import com.example.manage_revenue_ticket.Dto.request.LoyaltyPointsRequest;
import com.example.manage_revenue_ticket.entity.LoyaltyPoint;
import com.example.manage_revenue_ticket.entity.User;
import com.example.manage_revenue_ticket.exception.ResourceNotFoundException;
import com.example.manage_revenue_ticket.repository.LoyaltyPointRepository;
import com.example.manage_revenue_ticket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoyaltyPointsService {
    @Autowired
    private LoyaltyPointRepository loyaltyPointRepository;

    @Autowired
    private UserRepository userRepository;


    // 🆕 CREATE - thêm giao dịch điểm mới
    public LoyaltyPoint create(LoyaltyPointsRequest request) {
        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(()->new ResourceNotFoundException("Không thấy người dùng này"));
        LoyaltyPoint entity = new LoyaltyPoint();
        entity.setCustomer(customer);
        entity.setPoints(request.getPoints());
        entity.setTransactionType(request.getTransactionType());
        entity.setDescription(request.getDescription());
        return loyaltyPointRepository.save(entity);
    }

    // ✏️ UPDATE - cập nhật giao dịch điểm (ví dụ chỉnh sửa mô tả hoặc điểm)
    public LoyaltyPoint update(Long id, LoyaltyPointsRequest request) {
        LoyaltyPoint existing = loyaltyPointRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch sử điểm có id: " + id));
        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(()->new ResourceNotFoundException("Không thấy người dùng này"));

        existing.setPoints(request.getPoints());
        existing.setTransactionType(request.getTransactionType());
        existing.setDescription(request.getDescription());
        existing.setCustomer(customer);
        return loyaltyPointRepository.save(existing);
    }
}
