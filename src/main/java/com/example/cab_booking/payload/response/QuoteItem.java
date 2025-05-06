// src/main/java/com/example/cab_booking/payload/response/QuoteItem.java
package com.example.cab_booking.payload.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class QuoteItem {
    Long   vehicleCategoryId;
    String vehicleCategory;
    Double distanceKm;
    Double price;            // rounded final estimate
    Double surgeMultiplier;  // for UI
}
