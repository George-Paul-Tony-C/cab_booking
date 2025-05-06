// src/main/java/com/example/cab_booking/controller/driver/DriverProfileController.java
package com.example.cab_booking.controller.driver;

import com.example.cab_booking.payload.request.DriverProfileRequest;
import com.example.cab_booking.payload.response.DriverProfileResponse;
import com.example.cab_booking.service.driver.DriverProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/driver/profile")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DRIVER')")
public class DriverProfileController {

    private final DriverProfileService service;

    /** GET /api/driver/profile */
    @GetMapping
    public DriverProfileResponse me() {
        return service.getMyProfile();
    }

    /** POST /api/driver/profile  (create or update) */
    @PostMapping
    public DriverProfileResponse createOrUpdate(
            @Valid @RequestBody DriverProfileRequest req) {
        return service.createOrUpdate(req);
    }
}
