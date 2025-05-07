package com.example.cab_booking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cab_booking.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
    // ReviewRepository.java
    Optional<Review> findByBooking_Id(Long bookingId);
    List<Review> findByDriver_Id(Long driverId);

}
