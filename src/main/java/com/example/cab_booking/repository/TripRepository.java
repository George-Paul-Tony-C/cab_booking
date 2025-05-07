// src/main/java/com/example/cab_booking/repository/TripRepository.java
package com.example.cab_booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cab_booking.model.Booking;
import com.example.cab_booking.model.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {
    // findByBooking
    Optional<Trip> findByBookingId(Long bookingId);
    Optional<Trip> findByBooking(Booking booking);

    /* returns the first row (lowest id) for that booking */
    Optional<Trip> findTopByBooking_IdOrderByIdAsc(Long bookingId);

    /* helper for startTrip() */
    boolean existsByBooking_Id(Long bookingId);
}
