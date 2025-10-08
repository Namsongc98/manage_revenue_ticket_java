package com.example.manage_revenue_ticket.service;

import com.example.manage_revenue_ticket.Dto.request.TicketResponseDto;
import com.example.manage_revenue_ticket.Enum.DriverStatus;
import com.example.manage_revenue_ticket.Enum.TripStatus;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.entity.Buses;
import com.example.manage_revenue_ticket.entity.Ticket;
import com.example.manage_revenue_ticket.entity.Trip;
import com.example.manage_revenue_ticket.entity.User;
import com.example.manage_revenue_ticket.exception.ResourceNotFoundException;
import com.example.manage_revenue_ticket.repository.BusesRepository;
import com.example.manage_revenue_ticket.repository.TicketRepository;
import com.example.manage_revenue_ticket.repository.TripRepository;
import com.example.manage_revenue_ticket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusesRepository busesRepository;


    public void createTicket(TicketResponseDto responseDto){

        Trip trip =  tripRepository.findById(responseDto.getTripId())
                .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy chuyến xe " + responseDto.getTripId()));
        if(trip.getStatus() == TripStatus.ONGOING){
            throw new IllegalArgumentException("Chuyến xe này đang trong chuyến!");
        };
        User customer = userRepository.findById(responseDto.getCustomerId())
                .orElseThrow(()->new ResourceNotFoundException("Không thấy người dùng này"));

        User seller = userRepository.findById(responseDto.getSellerId())
                .orElseThrow(()->new ResourceNotFoundException("Không thấy người bán này"));

        if(customer.getRole() == UserRole.DRIVER && customer.getDriverStatus() == DriverStatus.ACTIVE){
            throw new IllegalArgumentException("Bác tài số" + responseDto.getCustomerId() + " đang chạy chuyến khác!");
        }
        if(customer.getRole() == UserRole.DRIVER && customer.getDriverStatus() == DriverStatus.INACTIVE){
            throw new IllegalArgumentException("Bác tài số" + responseDto.getCustomerId() + " không thể chạy chuyến!");
        }

//        Buses buses = busesRepository.findById(trip)
        List<Object[]> results = ticketRepository.findTripBusRouteInfo(responseDto.getTripId());

        List<Map<String, Object>> dataList = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("busId", row[0]);
            map.put("bienSoXe", row[1]);
            map.put("dungTich", row[2]);
            map.put("busStatus", row[3]);
            map.put("doanhThu", row[4]);
            map.put("khoiHanhTime", row[5]);
            map.put("denTime", row[6]);
            map.put("tenChuyen", row[7]);
            map.put("choKhoiHang", row[8]);
            map.put("choDen", row[9]);
            map.put("km", row[10]);
            dataList.add(map);
        }
        System.out.println(results);
    };

}
