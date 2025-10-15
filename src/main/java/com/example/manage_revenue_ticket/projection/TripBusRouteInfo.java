package com.example.manage_revenue_ticket.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TripBusRouteInfo {
    Long getBusId();
    String getBienSoXe();
    Integer getDungTich();
    String getBusStatus();
    BigDecimal getDoanhThu();
    LocalDateTime getKhoiHanhTime();
    LocalDateTime getDenTime();
    String getTenChuyen();
    String getChoKhoiHang();
    String getChoDen();
    Double getKm();
}
