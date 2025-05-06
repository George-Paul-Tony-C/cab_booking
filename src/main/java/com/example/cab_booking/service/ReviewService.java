// src/main/java/com/example/cab_booking/service/ReviewService.java
package com.example.cab_booking.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.cab_booking.enums.BookingStatus;
import com.example.cab_booking.model.Booking;
import com.example.cab_booking.model.DriverProfile;
import com.example.cab_booking.model.Review;
import com.example.cab_booking.model.Trip;
import com.example.cab_booking.model.User;
import com.example.cab_booking.payload.request.ReviewRequest;
import com.example.cab_booking.payload.response.DriverRatingSummary;
import com.example.cab_booking.payload.response.ReviewResponse;
import com.example.cab_booking.repository.BookingRepository;
import com.example.cab_booking.repository.ReviewRepository;
import com.example.cab_booking.repository.TripRepository;
import com.example.cab_booking.repository.UserRepository;
import com.example.cab_booking.security.services.UserDetailsImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository   reviewRepo;
    private final BookingRepository  bookingRepo;
    private final TripRepository     tripRepo;
    private final UserRepository     userRepo;

    /* ---------- create ---------- */
    public ReviewResponse create(ReviewRequest req) {

        User customer = currentUser();

        // 1. booking must belong to this customer + completed
        Booking b = bookingRepo.findById(req.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!b.getCustomer().getId().equals(customer.getId()))
            throw new IllegalStateException("Not your booking");
        if (b.getBookingStatus() != BookingStatus.COMPLETED)
            throw new IllegalStateException("Trip not completed");

        // 2. one review per booking
        if (reviewRepo.findByBooking_Id(b.getId()).isPresent())
            throw new IllegalStateException("Review already submitted");

        // 3. fetch the completed Trip to get vehicle
        Trip trip = tripRepo.findByBooking(b)
                .orElseThrow(() -> new IllegalStateException("Trip row missing"));

        // 4. build entity
        Review r = Review.builder()
                .booking(b)
                .customer(customer)
                .driver(b.getDriver())
                .vehicle(trip.getVehicle())
                .rating(req.getRating())
                .reviewMsg(req.getMessage())
                .build();

        reviewRepo.save(r);

        // 5. update driver’s averages
        aggregateDriverRating(b.getDriver());

        return toDto(r);
    }


    /* ---------- public fetch ---------- */
    public List<ReviewResponse> driverReviews(Long driverId) {
        return reviewRepo.findByDriver_Id(driverId)
                .stream().map(ReviewService::toDto).collect(Collectors.toList());
    }

    public DriverRatingSummary driverSummary(Long driverId) {
        User d = userRepo.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        return new DriverRatingSummary(
                d.getId(), d.getUsername(), d.getDriverProfile().getRatingAvg(),
                d.getDriverProfile().getTotalTrips());   // reuse field for count
    }

    /* ---------- helpers ---------- */

    private void aggregateDriverRating(User driver) {
        List<Review> list = reviewRepo.findByDriver_Id(driver.getId());
        double avg = list.stream().mapToInt(Review::getRating).average().orElse(0.0);

        DriverProfile dp = driver.getDriverProfile();
        dp.setRatingAvg(avg);
        dp.setTotalTrips((long) list.size());
    }

    private static ReviewResponse toDto(Review r) {
        return ReviewResponse.builder()
                .id(r.getId())
                .driverId(r.getDriver().getId())
                .customer(r.getCustomer().getUsername())
                .rating(r.getRating())
                .message(r.getReviewMsg())
                .createdAt(r.getCreatedAt())
                .build();
    }

    private User currentUser() {
        Object pr = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (pr instanceof UserDetailsImpl u)
            return userRepo.findById(u.getId()).orElseThrow();
        throw new IllegalStateException("No auth user");
    }
}
