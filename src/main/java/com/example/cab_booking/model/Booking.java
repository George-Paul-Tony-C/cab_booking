// src/main/java/com/example/cab_booking/model/Booking.java
package com.example.cab_booking.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.example.cab_booking.enums.BookingStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "bookings")
public class Booking {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne @JoinColumn(name = "route_id")
    private Route route;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private String otp;
    private LocalDateTime pickupTimeRequested;

    private String cancelledBy;                // USER | DRIVER | SYSTEM

    @CreationTimestamp
    private LocalDateTime createdAt;

    // inside Booking.java
    @ManyToOne @JoinColumn(name = "driver_id")
    private User driver;                // may be null (waiting)

    @ManyToOne @JoinColumn(name = "vehicle_category_id")
    private VehicleCategory vehicleCategory;

}
