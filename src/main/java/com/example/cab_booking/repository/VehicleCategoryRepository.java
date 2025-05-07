// src/main/java/com/example/cab_booking/repository/VehicleCategoryRepository.java

package com.example.cab_booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cab_booking.model.VehicleCategory;

public interface VehicleCategoryRepository extends JpaRepository<VehicleCategory, Long> {
    Optional<VehicleCategory> findByName(String name);
}
