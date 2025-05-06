// src/main/java/com/example/cab_booking/payload/request/QuoteRequest.java
package com.example.cab_booking.payload.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuoteRequest {

    @NotNull private Long fromPlaceId;
    @NotNull private Long toPlaceId;

    /** vehicle category IDs you want prices for (if null → all categories) */
    private List<Long> vehicleCategoryIds;
}
