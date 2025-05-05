package com.example.cab_booking.controller.admin;

import com.example.cab_booking.enums.Role;
import com.example.cab_booking.payload.response.UserProfileResponse;
import com.example.cab_booking.service.admin.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserAdminService adminService;

    @GetMapping
    public Page<UserProfileResponse> list(Pageable page) {
        return adminService.listUsers(page);
    }

    @GetMapping("/{id}")
    public UserProfileResponse one(@PathVariable Long id) {
        return adminService.getUser(id);
    }

    @PatchMapping("/{id}/role/{role}")
    public ResponseEntity<Void> changeRole(@PathVariable Long id,
                                           @PathVariable Role role) {
        adminService.changeRole(id, role);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/block")
    public ResponseEntity<Void> block(@PathVariable Long id) {
        adminService.blockUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/unblock")
    public ResponseEntity<Void> unblock(@PathVariable Long id) {
        adminService.unblockUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
