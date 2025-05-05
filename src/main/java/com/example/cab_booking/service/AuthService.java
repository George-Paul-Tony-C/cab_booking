// src/main/java/com/example/cab_booking/service/AuthService.java
package com.example.cab_booking.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.cab_booking.enums.Role;
import com.example.cab_booking.enums.UserStatus;
import com.example.cab_booking.model.User;
import com.example.cab_booking.payload.request.LoginRequest;
import com.example.cab_booking.payload.request.SignupRequest;
import com.example.cab_booking.payload.response.MessageResponse;
import com.example.cab_booking.repository.UserRepository;
import com.example.cab_booking.security.jwt.JwtUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository        userRepository;
    private final PasswordEncoder       encoder;
    private final JwtUtils              jwtUtils;

    /* ---------- helpers for controllers ---------- */

    public boolean usernameExists(String username) { return userRepository.existsByUsername(username); }
    public boolean emailExists   (String email)    { return userRepository.existsByEmail(email);   }
    public boolean phoneExists   (String phone)    { return userRepository.existsByPhone(phone);   }

    /* ---------- authentication ---------- */

    /**
     * Authenticate and return JWT for username **or** email.
     * Throws:
     * - UsernameNotFoundException  → no user with that identifier
     * - DisabledException          → user is BLOCKED
     * - BadCredentialsException    → wrong password
     */
    @Transactional          // <-- make sure the class or this method is transactional
    public String authenticateUser(LoginRequest req) {

        User user = userRepository.findByEmail(req.getEmail())
                    .or(() -> userRepository.findByUsername(req.getEmail()))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new DisabledException("Account is blocked");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        req.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        /* ---  set & persist last-login timestamp  --- */
        user.setLastLoginAt(LocalDateTime.now());   // ① set
        // if AuthService **is** annotated @Transactional, the flush happens automatically.
        // if you prefer to be explicit, or AuthService is NOT @Transactional, add:
        // userRepository.save(user);               // ② persist

        return jwtUtils.generateJwtToken(authentication);
    }

    /* ---------- registration ---------- */

    /**
     * Register a new user.
     * Controller should pre-check unique constraints.
     */
    public MessageResponse registerUser(SignupRequest req) {
        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .phone(req.getPhone())
                .password(encoder.encode(req.getPassword()))
                .role(req.getRole() != null ? req.getRole() : Role.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);
        return new MessageResponse("User registered successfully");
    }
}
