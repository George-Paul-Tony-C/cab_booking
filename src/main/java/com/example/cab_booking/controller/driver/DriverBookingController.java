// src/main/java/com/example/cab_booking/controller/driver/DriverBookingController.java
package com.example.cab_booking.controller.driver;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.cab_booking.payload.request.StartTripRequest;
import com.example.cab_booking.payload.response.BookingResponse;
import com.example.cab_booking.service.BookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/driver/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DRIVER')")
public class DriverBookingController {

    private final BookingService bookingService;

    @GetMapping("/waiting")
    public List<BookingResponse> waiting() {
        return bookingService.waitingForCurrentDriver();
    }

    @PatchMapping("/{id}/accept")
    public BookingResponse accept(@PathVariable Long id) {
        return bookingService.driverAccept(id);
    }

    @PatchMapping("/{id}/start")
    public BookingResponse start(@PathVariable Long id,
                                 @Valid @RequestBody StartTripRequest req) {
        return bookingService.startTrip(id, req.getOtp());
    }

    /* ---------- COMPLETE ---------- */

    /** Driver taps “Complete” – no body needed */
    @PatchMapping("/{id}/complete")
    public BookingResponse complete(@PathVariable Long id) {
        return bookingService.completeTrip(id);
    }
}
