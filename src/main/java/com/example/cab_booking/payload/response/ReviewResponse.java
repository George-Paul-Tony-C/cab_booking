// src/main/java/com/example/cab_booking/payload/response/ReviewResponse.java
package com.example.cab_booking.payload.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReviewResponse {
    Long   id;
    Long   driverId;
    String customer;
    Integer rating;
    String message;
    LocalDateTime createdAt;
}
