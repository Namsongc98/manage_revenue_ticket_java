package com.example.manage_revenue_ticket.controller;

import com.example.manage_revenue_ticket.Dto.request.TicketResponseDto;
import com.example.manage_revenue_ticket.Dto.response.BaseResponseDto;
import com.example.manage_revenue_ticket.anotation.NoAuth;
import com.example.manage_revenue_ticket.entity.BaseEntity;
import com.example.manage_revenue_ticket.entity.Ticket;
import com.example.manage_revenue_ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    @NoAuth
    public ResponseEntity<BaseResponseDto<Ticket>> addTicket(@RequestBody TicketResponseDto responseDto){
        ticketService.createTicket(responseDto);
        return ResponseEntity.ok(BaseResponseDto.success(201,"Add Successfully",null));
    }
}
