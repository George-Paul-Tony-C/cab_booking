// src/main/java/com/example/cab_booking/service/admin/AdminUserService.java
package com.example.cab_booking.service.admin;

import com.example.cab_booking.enums.Role;
import com.example.cab_booking.enums.UserStatus;
import com.example.cab_booking.model.User;
import com.example.cab_booking.payload.request.RoleUpdateRequest;
import com.example.cab_booking.payload.response.UserProfileResponse;
import com.example.cab_booking.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserService {

    private final UserRepository userRepo;

    /* ---------- helper ---------- */
    private static UserProfileResponse toProfile(User u) {
        return new UserProfileResponse(
                u.getId(), u.getUsername(), u.getEmail(), u.getRole(), u.getPhone());
    }

    /* ---------- queries ---------- */
    public Page<UserProfileResponse> listUsers(Pageable pageable) {
        return userRepo.findAll(pageable).map(AdminUserService::toProfile);
    }

    public UserProfileResponse getUser(Long id) {
        return toProfile(userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
    }

    /* ---------- commands ---------- */
    public void changeRole(Long id, RoleUpdateRequest req) {
        userRepo.findById(id).ifPresent(u -> u.setRole(req.getRole()));
    }

    public void blockUser(Long id) {
        userRepo.findById(id).ifPresent(u -> u.setStatus(UserStatus.BLOCKED));
    }

    public void unblockUser(Long id) {
        userRepo.findById(id).ifPresent(u -> u.setStatus(UserStatus.ACTIVE));
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
}
