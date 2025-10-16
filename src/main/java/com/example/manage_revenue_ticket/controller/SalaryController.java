package com.example.manage_revenue_ticket.controller;

import com.example.manage_revenue_ticket.Dto.request.UserRequestDto;
import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.anotation.RoleRequired;
import com.example.manage_revenue_ticket.entity.BaseSalary;
import com.example.manage_revenue_ticket.entity.Salary;
import com.example.manage_revenue_ticket.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    @PostMapping("/driver")
    @RoleRequired({UserRole.ADMIN,UserRole.EMPLOYEE})
    public ResponseEntity<BaseResponseDto<Page<Salary>>> fineSalaryMonth(
            @RequestParam byte month,
            @RequestParam short year
    ){
         salaryService.generateMonthlySalaries( month,  year);
        return ResponseEntity.ok(BaseResponseDto.success(200,"Create success", null));
    };

    @GetMapping
    @RoleRequired({UserRole.ADMIN,UserRole.EMPLOYEE})
    public  ResponseEntity<BaseResponseDto<Page<Salary>>> getSalaryMonth(
            @RequestParam byte month,
            @RequestParam short year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page,size);
        Page<Salary> listSalary = salaryService.selectSalaryMonth(month,year,pageable);
        return ResponseEntity.ok(BaseResponseDto.success(201,"Create success", listSalary));
    }

    @PostMapping
    @RoleRequired({UserRole.ADMIN,UserRole.EMPLOYEE})
    public ResponseEntity<BaseResponseDto<Salary>> fineSalaryMonth(@RequestParam byte month, @RequestParam short year, @RequestParam Long userId){
        Salary salary = salaryService.generateMonthlySalariesRoleUser( month,  year, userId);
        return ResponseEntity.ok(BaseResponseDto.success(201,"Create success", salary));
    };

}
