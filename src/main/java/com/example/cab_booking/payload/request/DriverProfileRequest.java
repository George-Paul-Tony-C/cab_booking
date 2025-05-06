// src/main/java/com/example/cab_booking/payload/request/DriverProfileRequest.java
package com.example.cab_booking.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DriverProfileRequest {

    @NotBlank
    private String licenseNo;

    @NotNull
    private LocalDate licenseExpiry;

    @NotBlank
    private String aadhaarNo;

    @NotBlank
    private String panNo;

    @Min(0)
    private Integer experienceYears;
}
