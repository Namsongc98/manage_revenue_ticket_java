package com.example.manage_revenue_ticket.controller;

import com.example.manage_revenue_ticket.Dto.request.RouteRequestDto;
import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.anotation.RoleRequired;
import com.example.manage_revenue_ticket.entity.Route;
import com.example.manage_revenue_ticket.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/route")
public class RouteController {

    @Autowired
    RouteService routeService;

    @PostMapping
    @RoleRequired({UserRole.ADMIN,UserRole.EMPLOYEE})
    ResponseEntity<BaseResponseDto<Route>> createRoute(@RequestBody RouteRequestDto requestDto){
        System.out.println(requestDto);
        Route response =  routeService.createRoute(requestDto);
        return ResponseEntity.ok(BaseResponseDto.success(201,"Create Successfully", response));
    }

    @PutMapping("/{routeId}")
    @RoleRequired({UserRole.ADMIN,UserRole.EMPLOYEE})
    ResponseEntity<BaseResponseDto<Route>> updateRoute(@PathVariable String routeId,@RequestBody RouteRequestDto requestDto){
        routeService.updateRoute(Long.parseLong(routeId) , requestDto);
        return ResponseEntity.ok(BaseResponseDto.success(201,"Update Successfully", null));
    }
}
