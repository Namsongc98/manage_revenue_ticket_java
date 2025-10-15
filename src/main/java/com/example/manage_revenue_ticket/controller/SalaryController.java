package com.example.manage_revenue_ticket.controller;

import com.example.manage_revenue_ticket.Dto.request.UserRequestDto;
import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.anotation.RoleRequired;
import com.example.manage_revenue_ticket.entity.Salary;
import com.example.manage_revenue_ticket.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    @GetMapping("/driver")
    @RoleRequired({UserRole.ADMIN,UserRole.EMPLOYEE})
    public ResponseEntity<BaseResponseDto<List<Salary>>> fineSalaryMonth(@RequestParam byte month, @RequestParam short year){
        List<Salary> salary = salaryService.generateMonthlySalaries( month,  year);
        return ResponseEntity.ok(BaseResponseDto.success(201,"Create success", salary));
    };

    @PostMapping
    @RoleRequired({UserRole.ADMIN,UserRole.EMPLOYEE})
    public ResponseEntity<BaseResponseDto<Salary>> fineSalaryMonth(@RequestParam byte month, @RequestParam short year, @RequestParam Long userId){
        Salary salary = salaryService.generateMonthlySalariesRoleUser( month,  year, userId);
        return ResponseEntity.ok(BaseResponseDto.success(201,"Create success", salary));
    };

}
