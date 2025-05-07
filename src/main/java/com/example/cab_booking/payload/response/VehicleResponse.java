package com.example.cab_booking.payload.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.cab_booking.model.Vehicle;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleResponse {

    private Long id;
    private String numberPlate;
    private String color;
    private Boolean available;
    private LocalDate insuranceExpiry;
    private LocalDate fitnessCertificateExpiry;
    private String driverName;
    private Long category_id;          // plain string to keep JSON simple
    private Long   currentPlaceId;    // so drivers know where the system thinks the car is
    private LocalDateTime createdAt;

    public static VehicleResponse fromEntity(Vehicle v) {
        return VehicleResponse.builder()
                .id(v.getId())
                .numberPlate(v.getNumberPlate())
                .color(v.getColor())
                .available(v.getAvailable())
                .insuranceExpiry(v.getInsuranceExpiry())
                .fitnessCertificateExpiry(v.getFitnessCertificateExpiry())
                .driverName(v.getDriver().getUsername())
                .category_id(v.getCategory().getId())
                .currentPlaceId(v.getCurrentPlace().getId())
                .createdAt(v.getCreatedAt())
                .build();
    }
}
