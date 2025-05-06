// src/main/java/com/example/cab_booking/payload/request/ReviewRequest.java
package com.example.cab_booking.payload.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull private Long bookingId;

    @Min(1) @Max(5)
    private Integer rating;

    @NotBlank
    private String message;
}
