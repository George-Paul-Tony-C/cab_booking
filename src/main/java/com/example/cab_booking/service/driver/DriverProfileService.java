// src/main/java/com/example/cab_booking/service/driver/DriverProfileService.java
package com.example.cab_booking.service.driver;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.cab_booking.model.DriverProfile;
import com.example.cab_booking.model.User;
import com.example.cab_booking.payload.request.DriverProfileRequest;
import com.example.cab_booking.payload.response.DriverProfileResponse;
import com.example.cab_booking.repository.DriverProfileRepository;
import com.example.cab_booking.repository.UserRepository;
import com.example.cab_booking.security.services.UserDetailsImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DriverProfileService {

    private final DriverProfileRepository driverRepo;
    private final UserRepository userRepo;

    /* ----------------- Helpers ----------------- */

    /** Fetch currently authenticated user entity */
    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetailsImpl principal)) {
            throw new IllegalStateException("No authenticated user in context");
        }
        return userRepo.findById(principal.getId())
                       .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    /** Map entity → response DTO */
    private static DriverProfileResponse toDto(DriverProfile d) {
        return DriverProfileResponse.builder()
                .userId(d.getId())
                .username(d.getUser().getUsername())
                .licenseNo(d.getLicenseNo())
                .licenseExpiry(d.getLicenseExpiry())
                .aadhaarNo(d.getAadhaarNo())
                .panNo(d.getPanNo())
                .experienceYears(d.getExperienceYears())
                .ratingAvg(d.getRatingAvg())
                .totalTrips(d.getTotalTrips())
                .joinedAt(d.getJoinedAt())
                .build();
    }

    /* ----------------- Driver self-service ----------------- */

    /** GET /api/driver/profile → fetch own profile */
    public DriverProfileResponse getMyProfile() {
        Long userId = currentUser().getId();

        return driverRepo.findById(userId)
                .map(DriverProfileService::toDto)
                .orElseThrow(() -> new IllegalStateException("No driver profile found for user ID: " + userId));
    }

    /** POST /api/driver/profile → create or update own profile */
    public DriverProfileResponse createOrUpdate(DriverProfileRequest req) {
        User user = currentUser();

        DriverProfile profile = driverRepo.findById(user.getId())
                .orElse(DriverProfile.builder().user(user).build());

        profile.setLicenseNo(req.getLicenseNo());
        profile.setLicenseExpiry(req.getLicenseExpiry());
        profile.setAadhaarNo(req.getAadhaarNo());
        profile.setPanNo(req.getPanNo());
        profile.setExperienceYears(req.getExperienceYears());

        driverRepo.save(profile);
        return toDto(profile);
    }

    /* ----------------- Admin-only ----------------- */

    /** GET /api/admin/drivers/{id}/profile → view driver profile by user ID */
    public DriverProfileResponse getByUserId(Long id) {
        return driverRepo.findById(id)
                .map(DriverProfileService::toDto)
                .orElseThrow(() -> new IllegalArgumentException("No driver profile found for user ID: " + id));
    }
}
