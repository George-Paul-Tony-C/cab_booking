// src/main/java/com/example/cab_booking/service/PaymentService.java
package com.example.cab_booking.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cab_booking.enums.PaymentMethod;
import com.example.cab_booking.enums.PaymentStatus;
import com.example.cab_booking.model.Payment;
import com.example.cab_booking.model.Trip;
import com.example.cab_booking.payload.response.PaymentResponse;
import com.example.cab_booking.payload.response.UpiIntentResponse;
import com.example.cab_booking.repository.PaymentRepository;
import com.example.cab_booking.repository.TripRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final TripRepository    tripRepo;

    /* ------------ util ------------- */
    private static PaymentResponse toDto(Payment p) {
        return PaymentResponse.builder()
                .id(p.getId())
                .tripId(p.getTrip().getId())
                .amount(p.getAmount())
                .method(p.getPaymentMethod())
                .status(p.getPaymentStatus())
                .capturedAt(p.getCapturedAt())
                .build();
    }

    /* ------------ cash ------------- */

    public PaymentResponse confirmCash(Long tripId) {
        Trip t = tripRepo.findById(tripId).orElseThrow();
        Payment p = paymentRepo.findByTrip(t).orElseThrow();

        p.setPaymentStatus(PaymentStatus.PAID);
        p.setCapturedAt(LocalDateTime.now());
        return toDto(p);
    }

    /* ------------ UPI --------------- */

    /** Create a pretend UPI deep-link; status remains PENDING until webhook */
    public UpiIntentResponse createUpiIntent(Long tripId) {
        Trip t = tripRepo.findById(tripId).orElseThrow();
        Payment p = paymentRepo.findByTrip(t).orElseThrow();

        p.setPaymentMethod(PaymentMethod.UPI);
        // In real life generate a proper payload / order with Razorpay, etc.
        String upiUrl = "upi://pay?pa=driver@upi&pn=CabBooking&am=" + p.getAmount()
                + "&tr=" + p.getId();
        return new UpiIntentResponse(upiUrl);
    }

    /** Simulated webhook / success redirect */
    public PaymentResponse confirmUpiPaid(Long paymentId) {
        Payment p = paymentRepo.findById(paymentId).orElseThrow();
        p.setPaymentStatus(PaymentStatus.PAID);
        p.setCapturedAt(LocalDateTime.now());
        return toDto(p);
    }

    /* ------------ listing ------------ */

    public List<PaymentResponse> driverPayments(Long driverId, PaymentStatus status) {
        return paymentRepo.findByTrip_Driver_IdAndPaymentStatus(driverId, status)
                          .stream().map(PaymentService::toDto).collect(Collectors.toList());
    }
}
