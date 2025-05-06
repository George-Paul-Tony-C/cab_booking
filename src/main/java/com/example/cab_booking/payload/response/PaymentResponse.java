// src/main/java/com/example/cab_booking/payload/response/PaymentResponse.java
package com.example.cab_booking.payload.response;

import java.time.LocalDateTime;

import com.example.cab_booking.enums.PaymentMethod;
import com.example.cab_booking.enums.PaymentStatus;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaymentResponse {
    Long   id;
    Long   tripId;
    Double amount;
    PaymentMethod method;
    PaymentStatus status;
    LocalDateTime capturedAt;
}
