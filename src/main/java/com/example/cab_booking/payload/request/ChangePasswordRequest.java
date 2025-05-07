// src/main/java/com/example/cab_booking/payload/request/ChangePasswordRequest.java
package com.example.cab_booking.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank
    private String currentPassword;

    @NotBlank
    @Size(min = 6, max = 120)
    private String newPassword;
}
