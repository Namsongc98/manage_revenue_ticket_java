package com.example.manage_revenue_ticket.service;


import com.example.manage_revenue_ticket.Dto.request.TripRequestDto;
import com.example.manage_revenue_ticket.Enum.BusStatus;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.Enum.DriverStatus;
import com.example.manage_revenue_ticket.entity.Buses;
import com.example.manage_revenue_ticket.entity.Route;
import com.example.manage_revenue_ticket.entity.Trip;
import com.example.manage_revenue_ticket.entity.User;
import com.example.manage_revenue_ticket.exception.ResourceNotFoundException;
import com.example.manage_revenue_ticket.repository.BusesRepository;
import com.example.manage_revenue_ticket.repository.RouteRepository;
import com.example.manage_revenue_ticket.repository.TripRepository;
import com.example.manage_revenue_ticket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private  RouteRepository routeRepository;
    @Autowired
    private  BusesRepository busRepository;
    @Autowired
    private  UserRepository userRepository;

   // Tạo chuyến
    public Trip createTrip(TripRequestDto requestDto){
        Route route = routeRepository.findById(requestDto.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tuyến có ID: " + requestDto.getRouteId()));

        Buses bus = busRepository.findById(requestDto.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe có ID: " + requestDto.getBusId()));

        User driver = userRepository.findById(requestDto.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài xế có ID: "+ requestDto.getDriverId()));
        if (driver.getRole() != UserRole.DRIVER) {
            throw new IllegalArgumentException("Người dùng ID " + requestDto.getDriverId() + " không phải là tài xế (DRIVER).");
        }

        if (driver.getDriverStatus() == DriverStatus.ACTIVE) {
            throw new IllegalArgumentException("Tài xế số" + driver.getId() + " đang chạy chuyến khác.");
        }
        if (driver.getDriverStatus() == DriverStatus.INACTIVE) {
            throw new IllegalArgumentException("Tài xế số" + driver.getId() + " không hoạt động.");
        }
        if (bus.getStatus() == BusStatus.INACTIVE) {
            throw new IllegalArgumentException("Bus có biển " + bus.getPlateNumber() + " đang gặp vấn đề không thể chạy.");
        }
        if (bus.getStatus() == BusStatus.ACTIVE) {
            throw new IllegalArgumentException("Bus có biển " + bus.getPlateNumber() + " đang chạy chuyển khác.");
        }
            Trip trip = Trip.builder()
                    .arrivalTime(requestDto.getArrivalTime())
                    .revenue(requestDto.getRevenue())
                    .departureTime(requestDto.getDepartureTime())
                    .status(requestDto.getStatus())
                    .bus(bus)
                    .driver(driver)
                    .route(route)
                    .build();

        Trip savedTrip = tripRepository.save(trip);
        bus.setStatus(BusStatus.ACTIVE);
        busRepository.save(bus);
        driver.setDriverStatus(DriverStatus.ACTIVE);
        userRepository.save(driver);
        return savedTrip;
    }
    // update chuyến xe
    public Trip updateTrip(Long tripId, TripRequestDto requestDto){
        Trip trip;
        trip = tripRepository.findById(tripId)
                .orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy tuyến có ID: " +  tripId));
        if (requestDto.getRouteId() != null) {
            Route route = routeRepository.findById(requestDto.getRouteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tuyến có ID: " + requestDto.getRouteId()));
            trip.setRoute(route);
        }
        Buses bus;
        if (requestDto.getBusId() != null) {
             bus = busRepository.findById(requestDto.getBusId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe có ID: " + requestDto.getBusId()));
            if (bus.getStatus() == BusStatus.INACTIVE) {
                throw new IllegalArgumentException("Bus có biển " + bus.getPlateNumber() + " đang gặp vấn đề không thể chạy.");
            }
            if (bus.getStatus() == BusStatus.ACTIVE) {
                throw new IllegalArgumentException("Bus có biển " + bus.getPlateNumber() + " đang chạy chuyển khác.");
            }
            trip.setBus(bus);
            bus.setStatus(BusStatus.ACTIVE);
            busRepository.save(bus);
        }
        User driver;
        if (requestDto.getDriverId() != null) {
             driver = userRepository.findById(requestDto.getDriverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài xế có ID: " + requestDto.getDriverId()));
            if (driver.getRole() != UserRole.DRIVER) {
                throw new IllegalArgumentException("Người dùng ID " + requestDto.getDriverId() + " không phải là tài xế (DRIVER).");
            }
            if (driver.getDriverStatus() == DriverStatus.ACTIVE) {
                throw new IllegalArgumentException("Tài xế số" + driver.getId() + " đang chạy chuyến khác.");
            }
            if (driver.getDriverStatus() == DriverStatus.INACTIVE) {
                throw new IllegalArgumentException("Tài xế số" + driver.getId() + " không hoạt động.");
            }
            trip.setDriver(driver);
            driver.setDriverStatus(DriverStatus.ACTIVE);
            userRepository.save(driver);
        }
        if (requestDto.getDepartureTime() != null) trip.setDepartureTime(requestDto.getDepartureTime());
        if (requestDto.getArrivalTime() != null) trip.setArrivalTime(requestDto.getArrivalTime());
        if (requestDto.getStatus() != null) trip.setStatus(requestDto.getStatus());
        if (requestDto.getRevenue() != null) trip.setRevenue(requestDto.getRevenue());
        return tripRepository.save(trip);
    }
    // lấy chuyến xe đang lên lịch
    public Page<Map<String, Object>> getTripScheduled(Pageable pageable){
        Page<Object[]> rows =  tripRepository.findTripBusRouteInfo(pageable);
        List<Map<String, Object>> content = rows.getContent().stream().map(row -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("busId", row[0]);
            map.put("plateNumber", row[1]);
            map.put("capacity", row[2]);
            map.put("busStatus", row[3]);
            map.put("revenue", row[4]);
            map.put("departureTime", row[5]);
            map.put("arrivalTime", row[6]);
            map.put("nameTrip", row[7]);
            map.put("startPoint", row[8]);
            map.put("enPoint", row[9]);
            map.put("km", row[10]);
            return map;
        }).collect(Collectors.toList());
        return new PageImpl<>(content, pageable, rows.getTotalElements());
    }
}
