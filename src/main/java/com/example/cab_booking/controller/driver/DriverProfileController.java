// src/main/java/com/example/cab_booking/controller/driver/DriverProfileController.java
package com.example.cab_booking.controller.driver;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cab_booking.payload.request.DriverProfileRequest;
import com.example.cab_booking.payload.response.DriverProfileResponse;
import com.example.cab_booking.service.driver.DriverProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/driver/profile")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DRIVER')")
public class DriverProfileController {

    private final DriverProfileService service;

    @GetMapping
    public DriverProfileResponse me() {
        return service.getMyProfile();
    }

    @PostMapping
    public DriverProfileResponse createOrUpdate(@Valid @RequestBody DriverProfileRequest req) {
        return service.createOrUpdate(req);
    }
}

