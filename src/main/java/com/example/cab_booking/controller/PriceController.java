// src/main/java/com/example/cab_booking/controller/PriceController.java
package com.example.cab_booking.controller;

import com.example.cab_booking.payload.response.PriceQuote;
import com.example.cab_booking.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    @GetMapping
    public List<PriceQuote> quote(
            @RequestParam("from") Long from,
            @RequestParam("to")   Long to,
            @RequestParam(value = "when", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime when) {

        return priceService.quoteAll(from, to,
                when == null ? LocalDateTime.now() : when);
    }
}
