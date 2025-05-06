// src/main/java/com/example/cab_booking/service/EarningsService.java
package com.example.cab_booking.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.cab_booking.enums.PaymentStatus;
import com.example.cab_booking.payload.response.DailyEarningResponse;
import com.example.cab_booking.payload.response.EarningSummary;
import com.example.cab_booking.projection.EarningSlice;
import com.example.cab_booking.repository.PaymentRepository;
import com.example.cab_booking.security.services.UserDetailsImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EarningsService {

    private final PaymentRepository paymentRepo;

    private Long driverId() {
        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((UserDetailsImpl) p).getId();
    }

    public List<DailyEarningResponse> daily(LocalDate from, LocalDate to) {
        List<EarningSlice> raw = paymentRepo.findDailySlices(
                driverId(), PaymentStatus.PAID, from, to);

        return raw.stream()
                  .map(s -> new DailyEarningResponse(s.getDay(), s.getTotal()))
                  .collect(Collectors.toList());
    }

    public EarningSummary summary(LocalDate from, LocalDate to) {
        List<EarningSlice> raw = paymentRepo.findDailySlices(
                driverId(), PaymentStatus.PAID, from, to);

        double total = raw.stream().mapToDouble(EarningSlice::getTotal).sum();
        long trips   = raw.stream().count(); // each slice = one or more trips that day

        return new EarningSummary(total, trips);
    }
}
