// src/main/java/com/example/cab_booking/payload/response/PriceQuote.java
package com.example.cab_booking.payload.response;

public record PriceQuote(Long categoryId,
                         String categoryName,
                         double totalFare,
                         double surgeMultiplier,
                         double bookingFee) {}
