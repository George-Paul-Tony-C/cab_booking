// src/main/java/com/example/cab_booking/payload/response/PriceQuoteResponse.java
package com.example.cab_booking.payload.response;

import lombok.Builder;
import lombok.Value;

/**
 * One fare estimate for a particular vehicle category.
 */
@Value
@Builder
public class PriceQuoteResponse {

    /* route info */
    Long   fromPlaceId;
    Long   toPlaceId;

    /* category */
    Long   vehicleCategoryId;
    String vehicleCategoryName;

    /* distance / eta */
    Double distanceKm;
    Integer etaMin;

    /* fare breakdown */
    Double baseFare;
    Double surgeMultiplier;
    Double bookingFee;
    Double total;              // final rounded fare
}
