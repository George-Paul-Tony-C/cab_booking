package com.example.cab_booking.payload.request;

import java.time.LocalDate;

import com.example.cab_booking.model.VehicleCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleRequest {

    @NotBlank
    private String numberPlate;

    private String color;

    /** Vehicle body-type / size class */
    @NotNull
    private VehicleCategory category;

    /** true = accepting rides */
    private Boolean available = true;

    /** FK of the place the vehicle is currently parked at */
    @NotNull
    private Long currentPlaceId;

    private LocalDate insuranceExpiry;
    private LocalDate fitnessCertificateExpiry;
}
