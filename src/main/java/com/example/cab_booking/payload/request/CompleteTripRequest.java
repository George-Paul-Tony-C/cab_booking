package com.example.cab_booking.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Body for PATCH /complete */
@Data
public class CompleteTripRequest {

    /** Total km actually travelled, e.g. 12.4 */
    @NotNull
    private Double distanceKm;

    /** Final fare charged to rider (Rs) */
    @NotNull
    private Double fareFinal;
}
