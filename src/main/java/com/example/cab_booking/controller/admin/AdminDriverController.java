// src/main/java/com/example/cab_booking/controller/admin/AdminDriverController.java
package com.example.cab_booking.controller.admin;

import com.example.cab_booking.payload.response.DriverProfileResponse;
import com.example.cab_booking.service.driver.DriverProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/drivers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDriverController {

    private final DriverProfileService service;

    /** GET /api/admin/drivers/{id} */
    @GetMapping("/{id}")
    public DriverProfileResponse get(@PathVariable Long id) {
        return service.getByUserId(id);
    }
}
