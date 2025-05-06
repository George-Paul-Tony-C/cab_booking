package com.example.cab_booking.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cab_booking.payload.request.VehicleRequest;
import com.example.cab_booking.payload.response.VehicleResponse;
import com.example.cab_booking.service.VehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    /* ============== DRIVER-specific ============== */

    @GetMapping("/me")
    @PreAuthorize("hasRole('DRIVER')")
    public List<VehicleResponse> getMyVehicles() {
        return vehicleService.getMyVehicles();
    }

    @PostMapping("/me")
    @PreAuthorize("hasRole('DRIVER')")
    public VehicleResponse addVehicle(@Valid @RequestBody VehicleRequest req) {
        return vehicleService.addVehicle(req);
    }

    /** Patch only the mutable bits: availability, place, category, etc. */
    @PatchMapping("/me/{id}")
    @PreAuthorize("hasRole('DRIVER')")
    public VehicleResponse updateVehicle(@PathVariable Long id,
                                         @RequestBody VehicleRequest req) {
        return vehicleService.updateVehicle(id, req);
    }

    /* ============== public list ============== */

    @GetMapping
    public List<VehicleResponse> listAll() {
        return vehicleService.listAllVehicles();
    }
}
