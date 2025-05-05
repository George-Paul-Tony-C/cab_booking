// src/main/java/com/example/cab_booking/model/DriverProfile.java
package com.example.cab_booking.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "driver_profiles")
public class DriverProfile {

    @Id                                      // PK = FK → User
    private Long id;

    @OneToOne @MapsId
    @JoinColumn(name = "id")
    private User user;

    private String licenseNo;
    private LocalDate licenseExpiry;

    private String aadhaarNo;
    private String panNo;

    private Integer experienceYears;
    private Double ratingAvg;
    private Long totalTrips;

    @CreationTimestamp
    private LocalDateTime joinedAt;
}
