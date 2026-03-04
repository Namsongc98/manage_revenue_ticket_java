package com.example.manage_revenue_ticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ManageRevenueTicketApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManageRevenueTicketApplication.class, args);
	}

}
