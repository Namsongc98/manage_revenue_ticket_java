package com.example.manage_revenue_ticket.controller;

import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.anotation.RoleRequired;
import com.example.manage_revenue_ticket.projection.CustomerRevenueByRouteProjection;
import com.example.manage_revenue_ticket.projection.TopUserProjection;
import com.example.manage_revenue_ticket.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/revenue")
public class RevenueController {

    @Autowired
    private RevenueService revenueService;

    @GetMapping("/bus")
    @RoleRequired({UserRole.ADMIN, UserRole.EMPLOYEE})
    public ResponseEntity<?> getRevenueByBusAndDate(
            @RequestParam (required = false) Long busId,
            @RequestParam LocalDate date) {
        List<Map<String, Object>> revenueByBusAndDate = revenueService.getRevenueByBusAndDate(busId, date);
        if(revenueByBusAndDate.isEmpty()){
            return ResponseEntity.ok(BaseResponseDto.success(200, "xe không có doanh thu", null));
        }
        return ResponseEntity.ok(BaseResponseDto.success(200, "Revenue fetched", revenueByBusAndDate));
    }

    @GetMapping("/employee")
    @RoleRequired({UserRole.ADMIN, UserRole.EMPLOYEE})
    public ResponseEntity<?> getRevenueByCollectorAndDate(
            @RequestParam (required = false) Long userId,
            @RequestParam LocalDate date) {
        List<Map<String, Object>> revenueByBusAndDate = revenueService.getRevenueByEmployeeAndDate(userId, date);
        if(revenueByBusAndDate.isEmpty()){
            return ResponseEntity.ok(BaseResponseDto.success(200, "bán hàng không có doanh thu", null));
        }
        return ResponseEntity.ok(BaseResponseDto.success(200, "Revenue fetched", revenueByBusAndDate));
    }

    @GetMapping("/user")
    @RoleRequired({UserRole.ADMIN, UserRole.EMPLOYEE})
    public ResponseEntity<?> getRevenueByUserAndDate(
            @RequestParam (required = false) Long busId,
            @RequestParam (required = false) Integer date,
            @RequestParam (required = false) Integer month,
            @RequestParam Integer year
    ) {
        List<CustomerRevenueByRouteProjection> revenueByBusAndDate = revenueService.getRevenueByUserAndDate(busId, date,month,year);
        if(revenueByBusAndDate.isEmpty()){
            return ResponseEntity.ok(BaseResponseDto.success(200, "người dùng không có doanh thu", null));
        }
        return ResponseEntity.ok(BaseResponseDto.success(200, "Revenue fetched", revenueByBusAndDate));
    }

    @GetMapping("/bus/{busId}/range")
    public ResponseEntity<?> getRevenueByBusAndRange(
            @PathVariable Long busId,
            @RequestParam("from") LocalDate from,
            @RequestParam("to") LocalDate to) {

        BigDecimal total = revenueService.getRevenueByBusAndRange(busId, from, to);
        return ResponseEntity.ok(BaseResponseDto.success(200, "Revenue fetched", total));
    }

    @GetMapping("/bus/all")
    public ResponseEntity<?> getTotalRevenueByAllBuses(@RequestParam LocalDate date) {
        List<Map<String, Object>> data = revenueService.getRevenueAllBusesInDate(date);
        return ResponseEntity.ok(BaseResponseDto.success(200, "Revenue fetched", data));
    }

    @GetMapping("/top-employees")
    @RoleRequired({UserRole.ADMIN, UserRole.EMPLOYEE})
    public ResponseEntity<?> getTopEmployees(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam (required = false) Integer day,
            @RequestParam (required = false) Integer month,
            @RequestParam Integer year) {
        List<TopUserProjection> result = revenueService.getTopEmployees(limit,year,month, day);
        return ResponseEntity.ok(BaseResponseDto.success(200, "Top nhân viên bán vé", result));
    }

    @GetMapping("/top-customers")
    @RoleRequired({UserRole.ADMIN, UserRole.EMPLOYEE})
    public ResponseEntity<?> getTopCustomers(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam (required = false) Integer day,
            @RequestParam (required = false) Integer month,
            @RequestParam Integer year
            ) {
        List<TopUserProjection> result = revenueService.getTopCustomers(limit,year,month, day);
        return ResponseEntity.ok(BaseResponseDto.success(200, "Top khách hàng thân thiết", result));
    }

}
