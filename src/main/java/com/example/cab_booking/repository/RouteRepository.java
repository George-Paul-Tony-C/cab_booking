// src/main/java/com/example/cab_booking/repository/RouteRepository.java
package com.example.cab_booking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cab_booking.model.Place;
import com.example.cab_booking.model.Route;
import com.example.cab_booking.model.VehicleCategory;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    /**
     * Find a route for a specific origin, destination and vehicle category.
     * Adjust the method name if your entity fields are different.
     */
    Optional<Route> findByFromPlaceAndToPlaceAndVehicleCategory(Place from, Place to, VehicleCategory cat);

    List<Route> findByFromPlaceAndToPlace(Place from, Place to);
}
