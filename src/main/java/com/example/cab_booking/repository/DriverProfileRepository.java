// src/main/java/com/example/cab_booking/repository/DriverProfileRepository.java
package com.example.cab_booking.repository;

import com.example.cab_booking.model.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverProfileRepository extends JpaRepository<DriverProfile, Long> {
    // PK == user_id
}
