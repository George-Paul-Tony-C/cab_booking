// src/main/java/com/example/cab_booking/controller/ReviewController.java
package com.example.cab_booking.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cab_booking.payload.request.ReviewRequest;
import com.example.cab_booking.payload.response.DriverRatingSummary;
import com.example.cab_booking.payload.response.ReviewResponse;
import com.example.cab_booking.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /* CUSTOMER posts review */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ReviewResponse create(@Valid @RequestBody ReviewRequest req) {
        return reviewService.create(req);
    }

    /* Anyone fetch driver’s reviews */
    @GetMapping("/driver/{driverId}")
    public List<ReviewResponse> byDriver(@PathVariable Long driverId) {
        return reviewService.driverReviews(driverId);
    }

    /* Summary (avg + count) */
    @GetMapping("/driver/{driverId}/summary")
    public DriverRatingSummary summary(@PathVariable Long driverId) {
        return reviewService.driverSummary(driverId);
    }
}
