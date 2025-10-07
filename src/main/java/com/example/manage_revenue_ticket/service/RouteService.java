package com.example.manage_revenue_ticket.service;

import com.example.manage_revenue_ticket.Dto.request.RouteRequestDto;
import com.example.manage_revenue_ticket.Enum.BusStatus;
import com.example.manage_revenue_ticket.Enum.RouteStatus;
import com.example.manage_revenue_ticket.entity.Route;
import com.example.manage_revenue_ticket.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteService {
    @Autowired
    private RouteRepository projectRepository;


    public Route createRoute(RouteRequestDto requestDto){
        System.out.println(requestDto.getRouteName());
        Route route;
        route = Route.builder()
                .distanceKm(requestDto.getDistanceKm())
                .startPoint(requestDto.getStartPoint())
                .endPoint(requestDto.getEndPoint())
                .status(RouteStatus.valueOf(requestDto.getStatus().name()))
                .routeName(requestDto.getRouteName())
                .build();

       return projectRepository.save(route);
    }

    public void updateRoute(Long routeId, RouteRequestDto requestDto){
        Route route;
        route = projectRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe với id: " + routeId));
        route.setDistanceKm(requestDto.getDistanceKm());
        route.setStartPoint(requestDto.getStartPoint());
        route.setEndPoint(requestDto.getEndPoint());
        route.setStatus(requestDto.getStatus());
        route.setRouteName(requestDto.getRouteName());
         projectRepository.save(route);
    }

}
