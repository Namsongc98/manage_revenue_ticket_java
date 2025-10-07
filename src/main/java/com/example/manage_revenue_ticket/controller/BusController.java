package com.example.manage_revenue_ticket.controller;

import com.example.manage_revenue_ticket.Dto.request.BusRequest;
import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.anotation.NoAuth;
import com.example.manage_revenue_ticket.anotation.RoleRequired;
import com.example.manage_revenue_ticket.entity.BaseEntity;
import com.example.manage_revenue_ticket.entity.Buses;
import com.example.manage_revenue_ticket.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/bus")
public class BusController {

    @Autowired
    private BusService busService;

    @GetMapping
    @NoAuth
    ResponseEntity<BaseResponseDto<Page<Buses>>> getBuses(@PageableDefault(size = 10) Pageable pageable){
        Page<Buses> listBus = busService.getBuses(pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("content", listBus.getContent());
        response.put("currentPage", listBus.getNumber());
        response.put("totalItems", listBus.getTotalElements());
        response.put("totalPages", listBus.getTotalPages());
        return ResponseEntity.ok(BaseResponseDto.success(200,"Get Successfully", listBus));
    }

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
