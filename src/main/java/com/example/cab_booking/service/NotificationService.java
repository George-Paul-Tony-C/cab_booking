// src/main/java/com/example/cab_booking/service/NotificationService.java
package com.example.cab_booking.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cab_booking.model.Notification;
import com.example.cab_booking.model.User;
import com.example.cab_booking.payload.response.NotificationResponse;
import com.example.cab_booking.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository repo;

    /* ------ producer API ------ */
    public void push(User user, String message, String channel) {
        log.info("Notify [{}] via {} -> {}", user.getEmail(), channel, message);
        // later: send FCM / SMS / e-mail
    }

    /* ------ consumer API ------ */
    public List<NotificationResponse> myFeed(Long userId) {
        return repo.findByUser_IdOrderBySentAtDesc(userId)
                   .stream().map(NotificationResponse::fromEntity)
                   .collect(Collectors.toList());
    }

    public void markRead(Long id, Long userId) {
        Notification n = repo.findById(id).orElseThrow();
        if (!n.getUser().getId().equals(userId))
            throw new IllegalStateException("Not your notification");
        n.setReadFlag(true);
    }

    
}
