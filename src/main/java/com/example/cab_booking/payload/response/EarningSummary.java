// src/main/java/com/example/cab_booking/payload/response/EarningSummary.java
package com.example.cab_booking.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EarningSummary {
    private final Double grandTotal;
    private final Long   tripsPaid;
}
