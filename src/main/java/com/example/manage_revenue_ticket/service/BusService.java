package com.example.manage_revenue_ticket.service;

import com.example.manage_revenue_ticket.Dto.request.BusRequest;
import com.example.manage_revenue_ticket.Enum.BusStatus;
import com.example.manage_revenue_ticket.entity.Buses;
import com.example.manage_revenue_ticket.repository.BusesRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Service
public class BusService {

    @Autowired
    private BusesRepository busRepository;

    public Buses save(BusRequest request) {
        Buses bus;
            // Thêm mới xe
            if (busRepository.existsByPlateNumber(request.getPlateNumber())) {
                throw new RuntimeException("Biển số xe đã tồn tại: " + request.getPlateNumber());
            }

            bus = Buses.builder()
                    .plateNumber(request.getPlateNumber())
                    .capacity(request.getCapacity())
                    .status(BusStatus.valueOf(request.getStatus().toUpperCase()))
                    .build();
        return busRepository.save(bus);
    }
    public Buses update(Long busId,BusRequest request) {
        Buses bus;
            bus = busRepository.findById(busId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy xe với id: " + busId));
            bus.setPlateNumber(request.getPlateNumber());
            bus.setCapacity(request.getCapacity());
            bus.setStatus(BusStatus.valueOf(request.getStatus().toUpperCase()));
        return busRepository.save(bus);
    }
}
