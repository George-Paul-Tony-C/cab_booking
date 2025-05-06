// src/main/java/com/example/cab_booking/service/driver/DriverProfileService.java
package com.example.cab_booking.service.driver;

import com.example.cab_booking.model.DriverProfile;
import com.example.cab_booking.model.User;
import com.example.cab_booking.payload.request.DriverProfileRequest;
import com.example.cab_booking.payload.response.DriverProfileResponse;
import com.example.cab_booking.repository.DriverProfileRepository;
import com.example.cab_booking.repository.UserRepository;
import com.example.cab_booking.security.services.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DriverProfileService {

    private final DriverProfileRepository driverRepo;
    private final UserRepository          userRepo;

    /* -------------- helpers -------------- */

    private User currentUser() {
        UserDetailsImpl p = (UserDetailsImpl)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findById(p.getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

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

    /* -------------- self-service -------------- */

    public DriverProfileResponse getMyProfile() {
        return driverRepo.findById(currentUser().getId())
                .map(DriverProfileService::toDto)
                .orElseThrow(() -> new IllegalStateException("Driver profile not created"));
    }

    public DriverProfileResponse createOrUpdate(DriverProfileRequest req) {
        User u = currentUser();

        DriverProfile profile = driverRepo.findById(u.getId())
                .orElseGet(() -> DriverProfile.builder().user(u).build());

        profile.setLicenseNo(req.getLicenseNo());
        profile.setLicenseExpiry(req.getLicenseExpiry());
        profile.setAadhaarNo(req.getAadhaarNo());
        profile.setPanNo(req.getPanNo());
        profile.setExperienceYears(req.getExperienceYears());

        driverRepo.save(profile);
        return toDto(profile);
    }

    /* -------------- admin -------------- */

    public DriverProfileResponse getByUserId(Long id) {
        return driverRepo.findById(id)
                .map(DriverProfileService::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Driver profile not found"));
    }
}
