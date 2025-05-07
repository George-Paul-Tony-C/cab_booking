// src/main/java/com/example/cab_booking/payload/response/AvatarResponse.java
package com.example.cab_booking.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvatarResponse {
    private String url;          // public URL or relative path of stored image
}
