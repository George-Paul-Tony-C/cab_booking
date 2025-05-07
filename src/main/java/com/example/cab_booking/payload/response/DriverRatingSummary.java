// src/main/java/com/example/cab_booking/payload/response/DriverRatingSummary.java
package com.example.cab_booking.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DriverRatingSummary {
    private final Long   driverId;
    private final String driverName;
    private final Double avgRating;
    private final Long   totalReviews;
}
