// src/main/java/com/example/cab_booking/payload/request/StartTripRequest.java
package com.example.cab_booking.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StartTripRequest {
    @NotBlank
    private String otp;   // 4-digit code spoken by the customer
}
