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
import org.springframework.stereotype.Service;

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
            if (bus.getStatus() != BusStatus.INACTIVE) {
                throw new IllegalArgumentException("Bus có biển " + bus.getPlateNumber() + " đang gặp vấn đề không thể chạy.");
            }
            if (bus.getStatus() != BusStatus.ACTIVE) {
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
}
