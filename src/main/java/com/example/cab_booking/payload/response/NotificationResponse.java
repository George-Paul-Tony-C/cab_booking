// src/main/java/com/example/cab_booking/payload/response/NotificationResponse.java
package com.example.cab_booking.payload.response;

import java.time.LocalDateTime;

import com.example.cab_booking.model.Notification;

import lombok.Value;

@Value
public class NotificationResponse {
    Long id;
    String message;
    String channel;
    boolean read;
    LocalDateTime sentAt;

    public static NotificationResponse fromEntity(Notification n) {
        return new NotificationResponse(
                n.getId(), n.getMessage(), n.getChannel(), n.isReadFlag(), n.getSentAt());
    }
}
