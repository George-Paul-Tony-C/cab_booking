// src/main/java/com/example/cab_booking/controller/BookingController.java
package com.example.cab_booking.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cab_booking.payload.request.BookingRequest;
import com.example.cab_booking.payload.response.BookingResponse;
import com.example.cab_booking.payload.response.MessageResponse;
import com.example.cab_booking.service.BookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class BookingController {

    private final BookingService service;

    @GetMapping("/{id}")
    public BookingResponse getBookingsById(@PathVariable Long id){
        return service.getBookingById(id);
    }

    @PostMapping
    public BookingResponse create(@Valid @RequestBody BookingRequest req) {
        return service.createBooking(req);
    }

    @GetMapping("/me")
    public List<BookingResponse> myBookings() {
        return service.getMyBookings();
    }

    @PatchMapping("/{id}/cancel")
    public MessageResponse cancel(@PathVariable Long id) {
        service.cancelBooking(id);
        return new MessageResponse("Booking cancelled");
    }
}
