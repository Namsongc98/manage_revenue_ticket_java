package com.example.manage_revenue_ticket.controller;

import com.example.manage_revenue_ticket.Dto.request.TicketResponseDto;
import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import com.example.manage_revenue_ticket.Enum.UserRole;
import com.example.manage_revenue_ticket.anotation.NoAuth;
import com.example.manage_revenue_ticket.anotation.RoleRequired;
import com.example.manage_revenue_ticket.entity.BaseEntity;
import com.example.manage_revenue_ticket.entity.Ticket;
import com.example.manage_revenue_ticket.service.FileService;
import com.example.manage_revenue_ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private FileService excelService;

    @PostMapping
    @RoleRequired({UserRole.ADMIN,UserRole.EMPLOYEE})
    public ResponseEntity<BaseResponseDto<Ticket>> addTicket(@RequestBody TicketResponseDto responseDto){
        ticketService.createTicket(responseDto);
        return ResponseEntity.ok(BaseResponseDto.success(201,"Add Successfully",null));
    };
    // export excel tổng doang thu của 1 xe theo time
    @GetMapping("/summary/excel")
    @RoleRequired({UserRole.ADMIN,UserRole.EMPLOYEE})
    public ResponseEntity<byte[]> exportTicketSummaryToExcel(
            @RequestParam(required = false) Long busId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd:HH:mm:ss") LocalDateTime fromDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd:HH:mm:ss") LocalDateTime toDate) {

        try {
            List<Map<String, Object>> summaryData = ticketService.getTicketSummaryByBusAndTime(busId, fromDate, toDate);

            // Tạo file Excel từ dữ liệu
            ByteArrayOutputStream outputStream = excelService.exportTicketSummaryToExcel(summaryData);

            // Thiết lập các header cho response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "ticket_summary.xlsx");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // sửa ticket
    @PutMapping("/{tripId}")
    @RoleRequired({UserRole.ADMIN,UserRole.EMPLOYEE})
    public ResponseEntity<BaseResponseDto<Ticket>> updateTicket(@PathVariable String tripId, @RequestBody TicketResponseDto responseDto){
        ticketService.updateTicket(Long.parseLong(tripId), responseDto);
        return ResponseEntity.ok(BaseResponseDto.success(201,"update Successfully",null));
    }

    // ticket tổng doang thu của 1 xe theo time
    @GetMapping("/summary")
    @RoleRequired({UserRole.ADMIN,UserRole.EMPLOYEE})
    public ResponseEntity<BaseResponseDto<?>> getTicketSummaryByBusAndTime(
            @RequestParam(required = false) Long busId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        List<Map<String, Object>> result = ticketService.getTicketSummaryByBusAndTime(busId, fromDate, toDate);
        return ResponseEntity.ok(BaseResponseDto.success(201,"Get Successfully",result));
    }


}
