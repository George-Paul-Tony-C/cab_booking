// src/main/java/com/example/cab_booking/repository/BookingRepository.java
package com.example.cab_booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cab_booking.enums.BookingStatus;
import com.example.cab_booking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    /* single-field queries must use the exact Java field name */
    List<Booking> findByBookingStatus(BookingStatus status);

    /* used for driver queue */
    List<Booking> findByRoute_FromPlace_IdAndBookingStatus(Long fromPlaceId,
                                                           BookingStatus status);

    /* customer history */
    List<Booking> findByCustomer_Id(Long customerId);
}
