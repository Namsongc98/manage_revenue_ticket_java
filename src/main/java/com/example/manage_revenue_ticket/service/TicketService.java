package com.example.manage_revenue_ticket.service;

import com.example.manage_revenue_ticket.Dto.request.TicketResponseDto;
import com.example.manage_revenue_ticket.Enum.*;
import com.example.manage_revenue_ticket.entity.*;
import com.example.manage_revenue_ticket.exception.ResourceNotFoundException;
import com.example.manage_revenue_ticket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private BaseLoyaltyPointsService baseLoyaltyPointsService;

    @Autowired
    private LoyaltyPointRepository loyaltyPointRepository;

    @Autowired
    private BaseLoyaltyPointsRepository baseLoyaltyPointsRepository;

    @Autowired
    private LoyaltyPointsService loyaltyPointsService;

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
        Optional<LoyaltyPoint> loyaltyPoint = loyaltyPointRepository.findFirstByCustomerId(customer.getId());

        customer.setUserStatus(CustomerStatus.BOOKED);
        userRepository.save(customer);
        List<BaseLoyaltyPoints> baseLoyaltyPoints = baseLoyaltyPointsRepository.findLatestActiveByRole(UserRole.CUSTOMER.name());

        if(baseLoyaltyPoints.isEmpty()){
            throw new RuntimeException("Not Found");
        }
        BaseLoyaltyPoints  firstElement = baseLoyaltyPoints.getFirst();
        int ticketsPerPoint = firstElement.getTicketsPerPoint();
        if(loyaltyPoint.isEmpty()){

            LoyaltyPoint loyaltyPoints = LoyaltyPoint.builder()
                    .customer(customer)
                    .points(ticketsPerPoint )
                    .transactionType(TransactionType.EARN)
                    .description("Cộng điểm khi mua thêm vé")
                    .build();
             loyaltyPointRepository.save(loyaltyPoints);
        }else {
            // 3️⃣ Nếu đã có, kiểm tra tổng số vé để cộng thêm điểm
            LoyaltyPoint loyaltyPointOPT = loyaltyPoint.get();
            loyaltyPointOPT.setPoints(loyaltyPointOPT.getPoints() + ticketsPerPoint);
            loyaltyPointOPT.setUpdatedAt(LocalDateTime.now());
            loyaltyPointOPT.setTransactionType(TransactionType.EARN);
            loyaltyPointRepository.save(loyaltyPointOPT);
        }

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
