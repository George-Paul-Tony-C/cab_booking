// src/main/java/com/example/cab_booking/payload/response/DailyEarningResponse.java
package com.example.cab_booking.payload.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyEarningResponse {
    private final LocalDate day;
    private final Double    total;
}
