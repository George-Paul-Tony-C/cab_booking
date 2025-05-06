// src/main/java/com/example/cab_booking/controller/driver/EarningsController.java
package com.example.cab_booking.controller.driver;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cab_booking.payload.response.DailyEarningResponse;
import com.example.cab_booking.payload.response.EarningSummary;
import com.example.cab_booking.service.EarningsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/driver/earnings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DRIVER')")
public class EarningsController {

    private final EarningsService earnings;

    /* GET /api/driver/earnings/daily?from=2025-05-01&to=2025-05-31 */
    @GetMapping("/daily")
    public List<DailyEarningResponse> daily(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return earnings.daily(from, to);
    }

    /* GET /api/driver/earnings/summary?from=...&to=... */
    @GetMapping("/summary")
    public EarningSummary summary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return earnings.summary(from, to);
    }
}
