// src/main/java/com/example/cab_booking/controller/PaymentController.java
package com.example.cab_booking.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cab_booking.enums.PaymentStatus;
import com.example.cab_booking.payload.response.PaymentResponse;
import com.example.cab_booking.payload.response.UpiIntentResponse;
import com.example.cab_booking.security.services.UserDetailsImpl;
import com.example.cab_booking.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /* ---------- driver side: confirm cash ---------- */
    @PatchMapping("/trip/{tripId}/cash/confirm")
    @PreAuthorize("hasRole('DRIVER')")
    public PaymentResponse confirmCash(@PathVariable Long tripId) {
        return paymentService.confirmCash(tripId);
    }

    /* ---------- customer side: start UPI ---------- */
    @PostMapping("/trip/{tripId}/upi")
    public UpiIntentResponse initUpi(@PathVariable Long tripId) {
        return paymentService.createUpiIntent(tripId);
    }

    /* ---------- webhook simulation ---------- */
    @PatchMapping("/{paymentId}/upi/success")
    public PaymentResponse upiSuccess(@PathVariable Long paymentId) {
        return paymentService.confirmUpiPaid(paymentId);
    }

    /* ---------- driver settlement list ---------- */
    @GetMapping("/driver/me/paid")
    @PreAuthorize("hasRole('DRIVER')")
    public List<PaymentResponse> driverPaid() {
        Long driverId = currentUserId();                 // ← fixed lookup
        return paymentService.driverPayments(driverId, PaymentStatus.PAID);
    }

    /* ---------- utility ---------- */
    private Long currentUserId() {
        Object principal = SecurityContextHolder.getContext()
                                                .getAuthentication()
                                                .getPrincipal();
        if (principal instanceof UserDetailsImpl u) {
            return u.getId();
        }
        throw new IllegalStateException("No authenticated user");
    }
}
