// src/main/java/com/example/cab_booking/repository/PaymentRepository.java
package com.example.cab_booking.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.cab_booking.enums.PaymentStatus;
import com.example.cab_booking.model.Payment;
import com.example.cab_booking.model.Trip;
import com.example.cab_booking.projection.EarningSlice;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTrip(Trip trip);
    List<Payment> findByTrip_Driver_IdAndPaymentStatus(Long driverId, PaymentStatus status);

    @Query("""
        SELECT DATE(p.capturedAt)  AS day,
               SUM(p.amount)       AS total
          FROM Payment p
          WHERE p.trip.driver.id   = :driverId
            AND p.paymentStatus    = :status
            AND DATE(p.capturedAt) BETWEEN :from AND :to
          GROUP BY DATE(p.capturedAt)
          ORDER BY day DESC
        """)
    List<EarningSlice> findDailySlices(Long driverId,
                                       PaymentStatus status,
                                       LocalDate from,
                                       LocalDate to);
}
