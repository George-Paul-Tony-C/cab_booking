// src/main/java/com/example/cab_booking/model/Review.java
package com.example.cab_booking.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ---------- relationships ---------- */

    @ManyToOne @JoinColumn(name = "booking_id")
    private Booking booking;          // link to completed booking

    @ManyToOne @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    /* ---------- payload ---------- */

    @Column(nullable = false)
    private Integer rating;           // 1–5

    @Column(length = 1000)
    private String reviewMsg;

    /* ---------- audit ---------- */

    @CreationTimestamp
    private LocalDateTime createdAt;
}
