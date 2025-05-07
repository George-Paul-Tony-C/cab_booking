// src/main/java/com/example/cab_booking/repository/PlaceRepository.java
package com.example.cab_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cab_booking.model.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    // add custom look-ups later if you need them
}
