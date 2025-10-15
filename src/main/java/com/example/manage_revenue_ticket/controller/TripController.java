package com.example.manage_revenue_ticket.controller;

import com.example.manage_revenue_ticket.Dto.request.TripRequestDto;

import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.anotation.NoAuth;
import com.example.manage_revenue_ticket.anotation.RoleRequired;
import com.example.manage_revenue_ticket.entity.Trip;
import com.example.manage_revenue_ticket.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trip")
public class TripController {
    @Autowired
    private TripService tripService;

    @PostMapping
    @RoleRequired({UserRole.ADMIN,UserRole.EMPLOYEE})
    ResponseEntity<BaseResponseDto<Trip>> createTrip(@RequestBody TripRequestDto requestDto){
        Trip trip = tripService.createTrip(requestDto);
        return ResponseEntity.ok(BaseResponseDto.success(201,"create success", trip));
    }
    // các chuyến đi đã lên lịch
    @GetMapping("/scheduled")
    @RoleRequired({UserRole.ADMIN,UserRole.EMPLOYEE})
    ResponseEntity<BaseResponseDto<List<Map<String, Object>> >> getTripScheduled(){
        List<Map<String, Object>>  trip = tripService.getTripScheduled();
        return ResponseEntity.ok(BaseResponseDto.success(200,"create success", trip));
    }


    @PutMapping("/{tripId}")
    @RoleRequired({UserRole.ADMIN,UserRole.EMPLOYEE})
    ResponseEntity<BaseResponseDto<Trip>> updateTrip(@PathVariable String tripId,@RequestBody TripRequestDto requestDto ){
        Trip trip = tripService.updateTrip(Long.parseLong(tripId),requestDto);
        return ResponseEntity.ok(BaseResponseDto.success(201,"create success", trip));
    }

}
