// src/main/java/com/example/cab_booking/service/UserService.java
package com.example.cab_booking.service;

import com.example.cab_booking.model.User;
import com.example.cab_booking.payload.request.UpdateUserRequest;
import com.example.cab_booking.payload.response.UserProfileResponse;
import com.example.cab_booking.repository.UserRepository;
import com.example.cab_booking.security.services.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor              // Lombok ctor injection
@Transactional
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    /* ------------------------------------------------------------
       Helpers
       ------------------------------------------------------------ */

    /** Resolve the authenticated principal → User entity. */
    private User getCurrentUserEntity() {
        Authentication auth = SecurityContextHolder
                                  .getContext()
                                  .getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserDetailsImpl principal)) {
            throw new IllegalStateException("No authenticated user in context");
        }

        return userRepo.findById(principal.getId())
                       .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    /** Convert entity → DTO (centralised so the mapping stays DRY). */
    private static UserProfileResponse toProfile(User u) {
        return new UserProfileResponse(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getRole(),
                u.getPhone()
        );
    }

    /* ------------------------------------------------------------
       Public API
       ------------------------------------------------------------ */

    /** Read-only view of the logged-in user. */
    public UserProfileResponse getMyProfile() {
        return toProfile(getCurrentUserEntity());
    }

    /** Update permitted fields for the logged-in user. */
    public UserProfileResponse updateMyProfile(UpdateUserRequest req) {
        User user = getCurrentUserEntity();

        if (req.getUsername() != null && !req.getUsername().isBlank()) {
            user.setUsername(req.getUsername());
        }
        if (req.getEmail() != null && !req.getEmail().isBlank()) {
            user.setEmail(req.getEmail());
        }
        if (req.getPhone() != null && !req.getPhone().isBlank()) {
            user.setPhone(req.getPhone());
        }
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            user.setPassword(encoder.encode(req.getPassword()));
        }

        // @Transactional → changes flushed automatically, but explicit save is fine too
        userRepo.save(user);

        return toProfile(user);
    }
}
