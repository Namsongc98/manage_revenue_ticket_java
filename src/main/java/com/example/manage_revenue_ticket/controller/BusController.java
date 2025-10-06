package com.example.manage_revenue_ticket.controller;

import com.example.manage_revenue_ticket.Dto.request.BusRequest;
import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.anotation.RoleRequired;
import com.example.manage_revenue_ticket.entity.BaseEntity;
import com.example.manage_revenue_ticket.entity.Buses;
import com.example.manage_revenue_ticket.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/bus")
public class BusController {

    @Autowired
    private BusService busService;

    @PostMapping
    @RoleRequired(UserRole.ADMIN)
    public ResponseEntity<BaseResponseDto<Buses>> postBus(@RequestBody BusRequest request){
        busService.save(request);
        return ResponseEntity.ok(BaseResponseDto.success(201,"Post successfully",null));
    }

    @PutMapping("/{busId}")
    @RoleRequired(UserRole.ADMIN)
    public ResponseEntity<BaseResponseDto<Buses>> putBus(@PathVariable String busId,@RequestBody BusRequest request){
        Long busIdParse = Long.parseLong(busId);
        busService.update(busIdParse,request);
        return ResponseEntity.ok(BaseResponseDto.success(201,"Put successfully",null));
    }

}
