// src/main/java/com/example/cab_booking/model/Trip.java
package com.example.cab_booking.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.example.cab_booking.enums.PaymentStatus;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ---------- associations ---------- */

    @ManyToOne @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    /** Route chosen when booking was created */
    @ManyToOne @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne @JoinColumn(name = "pickup_place_id")
    private Place pickupPlace;

    @ManyToOne @JoinColumn(name = "drop_place_id")
    private Place dropPlace;          // filled on completion

    /* ---------- timing ---------- */

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /* ---------- metrics ---------- */

    private Double distanceActualKm;  // odometer / GPS on completion
    private Double surgeMultiplier;
    private Double fareFinal;         // final amount calculated

    /* ---------- audit ---------- */

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


    @CreationTimestamp
    private LocalDateTime createdAt;
}
