// src/main/java/com/example/cab_booking/payload/response/BookingResponse.java
package com.example.cab_booking.payload.response;

import java.time.LocalDateTime;

import com.example.cab_booking.enums.BookingStatus;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookingResponse {
    Long   id;
    BookingStatus status;
    String otp;

    /* route */
    String fromPlace;
    String toPlace;
    Double distanceKm;

    /* fare */
    String vehicleCategory;

    /* meta */
    LocalDateTime pickupTime;
    Long   driverId;     // null until accepted
}
