package com.example.cab_booking.payload.response;

public class JwtResponse {
    private String token;
    private String type;
    private Long id;
    private String username;
    private String email;
    private String role;

    // No-args constructor for Jackson
    public JwtResponse() {
        this.type = "Bearer";
    }

    // All-args constructor
    public JwtResponse(String token, Long id, String username, String email, String role) {
        this.token = token;
        this.type = "Bearer";
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // Getters & setters
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

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

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
