package com.example.cab_booking.service;

import java.util.List;
import static java.util.stream.Collectors.toList;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cab_booking.model.Place;          // <- assumes you already have this entity
import com.example.cab_booking.model.User;
import com.example.cab_booking.model.Vehicle;
import com.example.cab_booking.payload.request.VehicleRequest;
import com.example.cab_booking.payload.response.VehicleResponse;
import com.example.cab_booking.repository.PlaceRepository;
import com.example.cab_booking.repository.UserRepository;
import com.example.cab_booking.repository.VehicleRepository;
import com.example.cab_booking.security.services.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepo;
    private final UserRepository    userRepo;
    private final PlaceRepository   placeRepo;

    // -------------------------------------------------
    // helpers
    // -------------------------------------------------
    private User currentUser() {
        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (p instanceof UserDetailsImpl details) {
            return userRepo.findById(details.getId())
                    .orElseThrow(() -> new IllegalStateException("User not found"));
        }
        throw new IllegalStateException("Not authenticated");
    }

    private Place placeById(Long id) {
        return placeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Place not found: " + id));
    }

    // -------------------------------------------------
    // queries
    // -------------------------------------------------
    public List<VehicleResponse> getMyVehicles() {
        Long me = currentUser().getId();
        return vehicleRepo.findByDriverId(me).stream()
                          .map(VehicleResponse::fromEntity)
                          .collect(toList());
    }

    public List<VehicleResponse> listAllVehicles() {
        return vehicleRepo.findAll().stream()
                          .map(VehicleResponse::fromEntity)
                          .collect(toList());
    }

    // -------------------------------------------------
    // commands
    // -------------------------------------------------
    @Transactional
    public VehicleResponse addVehicle(VehicleRequest req) {

        if (vehicleRepo.existsByNumberPlate(req.getNumberPlate())) {
            throw new IllegalArgumentException("Vehicle already exists with same number plate");
        }

        Vehicle v = Vehicle.builder()
                .numberPlate(req.getNumberPlate())
                .color(req.getColor())
                .category(req.getCategory())
                .available(req.getAvailable())
                .insuranceExpiry(req.getInsuranceExpiry())
                .fitnessCertificateExpiry(req.getFitnessCertificateExpiry())
                .currentPlace(placeById(req.getCurrentPlaceId()))
                .driver(currentUser())
                .build();

        vehicleRepo.save(v);
        return VehicleResponse.fromEntity(v);
    }

    /** Driver can patch his/her own vehicle */
    @Transactional
    public VehicleResponse updateVehicle(Long id, VehicleRequest req) {

        Vehicle v = vehicleRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        // only owner may update
        if (!v.getDriver().getId().equals(currentUser().getId())) {
            throw new IllegalStateException("Access denied");
        }

        // -- optional fields: update only if present in JSON ----------------
        if (req.getColor() != null)               v.setColor(req.getColor());
        if (req.getAvailable() != null)           v.setAvailable(req.getAvailable());
        if (req.getCategory() != null)            v.setCategory(req.getCategory());
        if (req.getCurrentPlaceId() != null)      v.setCurrentPlace(placeById(req.getCurrentPlaceId()));
        if (req.getInsuranceExpiry() != null)     v.setInsuranceExpiry(req.getInsuranceExpiry());
        if (req.getFitnessCertificateExpiry()!=null)
                                                  v.setFitnessCertificateExpiry(req.getFitnessCertificateExpiry());

        // numberPlate stays unchanged to keep uniqueness constraints simple
        return VehicleResponse.fromEntity(v);
    }
}
