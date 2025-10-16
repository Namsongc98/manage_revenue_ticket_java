package com.example.manage_revenue_ticket.service;

import com.example.manage_revenue_ticket.Dto.request.TicketResponseDto;
import com.example.manage_revenue_ticket.Enum.CustomerStatus;
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

import java.time.LocalDateTime;
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

    // tạo vé
    public Ticket createTicket(TicketResponseDto responseDto){

        Trip trip =  tripRepository.findById(responseDto.getTripId())
                .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy chuyến xe " + responseDto.getTripId()));
        if(trip.getStatus() == TripStatus.ONGOING){
            throw new IllegalArgumentException("Chuyến xe này đang trong chuyến!");
        };
        User customer = userRepository.findById(responseDto.getCustomerId())
                .orElseThrow(()->new ResourceNotFoundException("Không thấy người dùng này"));

        User seller = userRepository.findById(responseDto.getSellerId())
                .orElseThrow(()->new ResourceNotFoundException("Không thấy người bán này"));

        if(customer.getUserStatus() == CustomerStatus.BOOKED){
            throw new IllegalArgumentException("Khách hàng đã đặt vé");
        };
        customer.setUserStatus(CustomerStatus.BOOKED);
        userRepository.save(customer);
        Buses bus = trip.getBus();
        int countTicket = ticketRepository.countTicketsByBusId(bus.getId());
        if(countTicket == bus.getCapacity()){
            throw  new ResourceNotFoundException("chuyến đi có số lượng "+ bus.getCapacity() + " số vé "+countTicket+" đã hết vé");
        }
        Ticket ticket = Ticket.builder()
                .trip(trip)
                .seller(seller)
                .customer(customer)
                .price(responseDto.getPrice())
                .seatNumber(responseDto.getSeatNumber())
                .issuedAt(responseDto.getIssuedAt())
                .build();
        ticketRepository.save(ticket);
        return ticket;
    };
    // update vé
    public Ticket updateTicket(Long ticketId, TicketResponseDto responseDto){

        Ticket ticket;
        ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy vé xe " + responseDto.getTripId()));

        Trip trip =  tripRepository.findById(responseDto.getTripId())
                .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy chuyến xe " + responseDto.getTripId()));
        if(trip.getStatus() == TripStatus.ONGOING){
            throw new IllegalArgumentException("Chuyến xe này đang trong chuyến!");
        };
        User customer = userRepository.findById(responseDto.getCustomerId())
                .orElseThrow(()->new ResourceNotFoundException("Không thấy người dùng này"));

        User seller = userRepository.findById(responseDto.getSellerId())
                .orElseThrow(()->new ResourceNotFoundException("Không thấy người bán này"));
        if(responseDto.getPrice() != null) ticket.setPrice(responseDto.getPrice());
        if(responseDto.getSeatNumber() !=null ) ticket.setSeatNumber(responseDto.getSeatNumber());
        ticket.setSeller(seller);
        ticket.setCustomer(customer);
        customer.setUserStatus(CustomerStatus.BOOKED);
        userRepository.save(customer);
        ticketRepository.save(ticket);
        return ticket;
    };

    public List<Map<String, Object>> getTicketSummaryByBusAndTime(Long busId, LocalDateTime fromDate, LocalDateTime toDate) {
        return ticketRepository.getTicketSummaryByBusAndTime(busId, fromDate, toDate);
    }
}
