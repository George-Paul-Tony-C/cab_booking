// src/main/java/com/example/cab_booking/payload/request/UpdateUserRequest.java
package com.example.cab_booking.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {

    /** New username (optional). */
    @Size(min = 3, max = 40)
    private String username;

    /** New email (optional). */
    @Email
    @Size(max = 100)
    private String email;

    /** New 10-digit phone (optional). */
    @Pattern(regexp = "^\\d{10}$",
             message = "Phone must be exactly 10 digits")
    private String phone;

    /** New password (optional). */
    @Size(min = 6, max = 120)
    private String password;

    public UpdateUserRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
