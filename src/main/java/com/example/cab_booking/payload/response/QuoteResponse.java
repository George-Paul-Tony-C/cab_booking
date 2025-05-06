// src/main/java/com/example/cab_booking/payload/response/QuoteResponse.java
package com.example.cab_booking.payload.response;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class QuoteResponse {
    String  from;
    String  to;
    List<QuoteItem> quotes;
}
