// src/main/java/com/example/cab_booking/repository/VehicleCategoryRepository.java
package com.example.cab_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cab_booking.model.VehicleCategory;

@Repository
public interface VehicleCategoryRepository extends JpaRepository<VehicleCategory, Long> {
    // optional: findByName(String name) etc.
}
