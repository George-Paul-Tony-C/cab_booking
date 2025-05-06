// src/main/java/com/example/cab_booking/repository/VehicleRepository.java
package com.example.cab_booking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cab_booking.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByDriverId(Long driverId);
    boolean existsByNumberPlate(String numberPlate);

    List<Vehicle> findByCurrentPlace_IdAndAvailableTrue(Long placeId);

    Optional<Vehicle> findFirstByDriver_IdAndAvailableTrue(Long driverId);
}
