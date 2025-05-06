// src/main/java/com/example/cab_booking/controller/admin/AdminUserController.java
package com.example.cab_booking.controller.admin;

import com.example.cab_booking.payload.request.RoleUpdateRequest;
import com.example.cab_booking.payload.response.MessageResponse;
import com.example.cab_booking.payload.response.UserProfileResponse;
import com.example.cab_booking.service.admin.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService service;

    /** GET /api/admin/users?page=…&size=… */
    @GetMapping
    public Page<UserProfileResponse> list(Pageable pageable) {
        return service.listUsers(pageable);
    }

    /** GET /api/admin/users/{id} */
    @GetMapping("/{id}")
    public UserProfileResponse one(@PathVariable Long id) {
        return service.getUser(id);
    }

    /** PATCH /api/admin/users/{id}/role */
    @PatchMapping("/{id}/role")
    public ResponseEntity<MessageResponse> changeRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleUpdateRequest req) {
        service.changeRole(id, req);
        return ResponseEntity.ok(new MessageResponse("Role updated"));
    }

    /** PATCH /api/admin/users/{id}/block */
    @PatchMapping("/{id}/block")
    public ResponseEntity<MessageResponse> block(@PathVariable Long id) {
        service.blockUser(id);
        return ResponseEntity.ok(new MessageResponse("User blocked"));
    }

    /** PATCH /api/admin/users/{id}/unblock */
    @PatchMapping("/{id}/unblock")
    public ResponseEntity<MessageResponse> unblock(@PathVariable Long id) {
        service.unblockUser(id);
        return ResponseEntity.ok(new MessageResponse("User unblocked"));
    }

    /** DELETE /api/admin/users/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.ok(new MessageResponse("User deleted"));
    }
}
