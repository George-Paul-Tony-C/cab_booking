// src/main/java/com/example/cab_booking/payload/request/BookingRequest.java
package com.example.cab_booking.payload.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {

    @NotNull private Long fromPlaceId;
    @NotNull private Long toPlaceId;
    @NotNull private Long vehicleCategoryId;

    /** Optional: if null → “now”. */
    @FutureOrPresent
    private LocalDateTime pickupTime;
}
