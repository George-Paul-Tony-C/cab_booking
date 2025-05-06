// src/main/java/com/example/cab_booking/controller/NotificationController.java
package com.example.cab_booking.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cab_booking.payload.response.NotificationResponse;
import com.example.cab_booking.security.services.UserDetailsImpl;
import com.example.cab_booking.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class NotificationController {

    private final NotificationService notify;

    private Long currentId() {
        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((UserDetailsImpl) p).getId();
    }

    /* GET /api/notifications → list newest first */
    @GetMapping
    public List<NotificationResponse> myFeed() {
        return notify.myFeed(currentId());
    }

    /* PATCH /api/notifications/{id}/read */
    @PatchMapping("/{id}/read")
    public void markRead(@PathVariable Long id) {
        notify.markRead(id, currentId());
    }
}
