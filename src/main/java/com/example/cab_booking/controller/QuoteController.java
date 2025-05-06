// src/main/java/com/example/cab_booking/controller/QuoteController.java
package com.example.cab_booking.controller;

import com.example.cab_booking.payload.request.QuoteRequest;
import com.example.cab_booking.payload.response.QuoteResponse;
import com.example.cab_booking.service.QuoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService quoteService;

    /** Public – no auth required */
    @PostMapping
    public QuoteResponse quote(@Valid @RequestBody QuoteRequest req) {
        return quoteService.getQuote(req);
    }
}
