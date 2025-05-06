// src/main/java/com/example/cab_booking/controller/UserController.java
package com.example.cab_booking.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.cab_booking.payload.request.ChangePasswordRequest;
import com.example.cab_booking.payload.request.UpdateUserRequest;
import com.example.cab_booking.payload.response.AvatarResponse;
import com.example.cab_booking.payload.response.MessageResponse;
import com.example.cab_booking.payload.response.UserProfileResponse;
import com.example.cab_booking.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /* ---------------- existing ---------------- */

    /** GET /api/user/me → current user’s profile */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    /** PUT /api/user/me → update current user (username / email / phone) */
    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMyProfile(
            @Valid @RequestBody UpdateUserRequest req) {
        return ResponseEntity.ok(userService.updateMyProfile(req));
    }

    /* ---------------- new ---------------- */

    /** PUT /api/user/password → change password */
    @PutMapping("/password")
    public ResponseEntity<MessageResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest req) {
        userService.changeMyPassword(req);
        return ResponseEntity.ok(new MessageResponse("Password updated successfully"));
    }

    /** PUT /api/user/avatar → upload or replace profile picture */
    @PutMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AvatarResponse> updateAvatar(
            @RequestPart("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok(userService.updateProfilePicture(file));
    }
}
