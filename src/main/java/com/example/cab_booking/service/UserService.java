// src/main/java/com/example/cab_booking/service/UserService.java
package com.example.cab_booking.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.cab_booking.model.User;
import com.example.cab_booking.payload.request.ChangePasswordRequest;
import com.example.cab_booking.payload.request.UpdateUserRequest;
import com.example.cab_booking.payload.response.AvatarResponse;
import com.example.cab_booking.payload.response.UserProfileResponse;
import com.example.cab_booking.repository.UserRepository;
import com.example.cab_booking.security.services.UserDetailsImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    /* folder where avatar images are stored (configurable) */
    @Value("${cab.avatar-folder:uploads/avatars}")
    private String avatarFolder;

    /* ------------------------------------------------------------
       Helpers
       ------------------------------------------------------------ */

    private User getCurrentUserEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetailsImpl principal)) {
            throw new IllegalStateException("No authenticated user in context");
        }
        return userRepo.findById(principal.getId())
                       .orElseThrow(() -> new IllegalStateException("User not found"));
    }

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

        userRepo.save(user);
        return toProfile(user);
    }

    /* ------------------------------------------------------------
       NEW: change password
       ------------------------------------------------------------ */
    public void changeMyPassword(ChangePasswordRequest req) {
        User user = getCurrentUserEntity();

        if (!encoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        user.setPassword(encoder.encode(req.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepo.save(user);
    }

    /* ------------------------------------------------------------
       NEW: upload / replace avatar
       ------------------------------------------------------------ */
    public AvatarResponse updateProfilePicture(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Provided file is empty");
        }

        Path dir = Path.of(avatarFolder);
        Files.createDirectories(dir);

        String ext = Optional.ofNullable(file.getOriginalFilename())
                             .filter(n -> n.contains("."))
                             .map(n -> n.substring(n.lastIndexOf('.')))
                             .orElse("");
        String filename = UUID.randomUUID() + ext;
        Path target = dir.resolve(filename);

        Files.write(target, file.getBytes());

        User user = getCurrentUserEntity();
        user.setProfilePictureUrl("/" + avatarFolder + "/" + filename);
        userRepo.save(user);

        return new AvatarResponse(user.getProfilePictureUrl());
    }
}
