package com.example.cab_booking.payload.response;

import com.example.cab_booking.enums.Role;

public class UserProfileResponse {

    private Long   id;
    private String username;
    private String email;
    private Role role;
    private String phone; 

    public UserProfileResponse() { }

    public UserProfileResponse(Long id, String username, String email, Role role , String phone) {
        this.id       = id;
        this.username = username;
        this.email    = email;
        this.role     = role;
        this.phone    = phone;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
}
