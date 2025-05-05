// src/main/java/com/example/cab_booking/security/services/UserDetailsImpl.java
package com.example.cab_booking.security.services;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.cab_booking.enums.UserStatus;
import com.example.cab_booking.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;
    private UserStatus status;                       // ACTIVE / BLOCKED

    /* ---------- builder ---------- */
    public static UserDetailsImpl build(User user) {
        String roleName = "ROLE_" + user.getRole().name();          // Spring expects ROLE_ prefix
        GrantedAuthority auth = new SimpleGrantedAuthority(roleName);

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                List.of(auth),          // singleton (JDK 9+)
                user.getStatus()
        );
    }

    /* ---------- UserDetails overrides ---------- */

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword()                                     { return password; }
    @Override public String getUsername()                                     { return username; }

    @Override public boolean isAccountNonExpired()  { return true; }

    @Override
    public boolean isAccountNonLocked()             {    // BLOCKED ⇒ locked
        return status != UserStatus.BLOCKED;
    }

    @Override public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled()                       {    // Spring uses this in auth filters
        return status == UserStatus.ACTIVE;
    }
}
