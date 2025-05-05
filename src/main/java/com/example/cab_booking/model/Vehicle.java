// src/main/java/com/example/cab_booking/model/Vehicle.java
package com.example.cab_booking.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "vehicles")
public class Vehicle {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 15)
    private String numberPlate;

    private String color;

    @ManyToOne @JoinColumn(name = "category_id")
    private VehicleCategory category;

    @ManyToOne @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne @JoinColumn(name = "place_id")
    private Place currentPlace;

    private Boolean available;

    private LocalDate insuranceExpiry;
    private LocalDate fitnessCertificateExpiry;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
