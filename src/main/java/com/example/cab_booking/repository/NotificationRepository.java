// src/main/java/com/example/cab_booking/repository/NotificationRepository.java
package com.example.cab_booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cab_booking.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser_IdOrderBySentAtDesc(Long userId);
}
