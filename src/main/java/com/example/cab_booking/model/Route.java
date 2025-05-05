// src/main/java/com/example/cab_booking/model/Route.java
package com.example.cab_booking.model;

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

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "routes")
public class Route {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "from_place_id")
    private Place fromPlace;

    @ManyToOne @JoinColumn(name = "to_place_id")
    private Place toPlace;

    @ManyToOne @JoinColumn(name = "vehicle_category_id")
    private VehicleCategory vehicleCategory;

    private Double distanceKm;
    private Integer estimatedTimeMin;
    private Double basePrice;
    private Double tollsEstimate;

    private Boolean isActive;
}
