// src/main/java/com/example/cab_booking/payload/response/UpiIntentResponse.java
package com.example.cab_booking.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpiIntentResponse {
    /** UPI deeplink / QR code URL for the client to open */
    private final String upiIntentUrl;
}
