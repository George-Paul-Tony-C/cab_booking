// src/main/java/com/example/cab_booking/model/PricingRule.java
package com.example.cab_booking.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Entity @Table(name = "pricing_rules")
public class PricingRule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "vehicle_category_id")
    private VehicleCategory vehicleCategory;

    private String city;                       // null → all cities
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;               // null → all days
    private String timeBand;                   // "18:00-22:00"

    private Double surgeMultiplier;
    private Double minFare;
    private Double bookingFee;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
