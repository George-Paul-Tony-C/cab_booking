// src/main/java/com/example/cab_booking/service/TripService.java
package com.example.cab_booking.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.cab_booking.enums.BookingStatus;
import com.example.cab_booking.enums.PaymentMethod;
import com.example.cab_booking.enums.PaymentStatus;
import com.example.cab_booking.model.Booking;
import com.example.cab_booking.model.Payment;
import com.example.cab_booking.model.Trip;
import com.example.cab_booking.model.Vehicle;
import com.example.cab_booking.repository.BookingRepository;
import com.example.cab_booking.repository.PaymentRepository;
import com.example.cab_booking.repository.TripRepository;
import com.example.cab_booking.repository.VehicleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TripService {

    private final BookingRepository bookingRepo;
    private final TripRepository    tripRepo;
    private final PaymentRepository paymentRepo;
    private final VehicleRepository vehicleRepo;

    public void completeTrip(Long bookingId, double distanceKm) {

        Booking b = bookingRepo.findById(bookingId).orElseThrow();
        if (b.getBookingStatus() != BookingStatus.STARTED)
            throw new IllegalStateException("Trip not started");

        /* --- update TRIP row --- */
        Trip t = tripRepo.findByBookingId(b.getId())
                .orElseThrow(() -> new IllegalStateException("Trip row missing"));

        t.setEndTime(LocalDateTime.now());
        t.setDistanceActualKm(distanceKm);

        /* very simple fare calc */
        double fare = b.getRoute().getBasePrice()
                   + (distanceKm * b.getVehicleCategory().getPricePerKm());
        t.setFareFinal(fare);

        tripRepo.save(t);

        /* --- flip booking & vehicle --- */
        b.setBookingStatus(BookingStatus.COMPLETED);

        Vehicle v = t.getVehicle();
        v.setAvailable(true);
        vehicleRepo.save(v);

        /* --- create PAYMENT row (cash MVP) --- */
        Payment p = Payment.builder()
                .trip(t)
                .amount(fare)
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(PaymentMethod.CASH)
                .capturedAt(null)
                .build();
        paymentRepo.save(p);
    }
}
