// src/main/java/com/example/cab_booking/payload/response/DriverProfileResponse.java
package com.example.cab_booking.payload.response;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
@Builder
public class DriverProfileResponse {

    Long   userId;
    String username;

    /* docs */
    String licenseNo;
    LocalDate licenseExpiry;
    String aadhaarNo;
    String panNo;

    /* stats */
    Integer experienceYears;
    Double  ratingAvg;
    Long    totalTrips;

    /* meta */
    LocalDateTime joinedAt;
}
