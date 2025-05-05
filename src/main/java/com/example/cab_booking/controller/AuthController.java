// src/main/java/com/example/cab_booking/controller/AuthController.java
package com.example.cab_booking.controller;

import com.example.cab_booking.payload.request.LoginRequest;
import com.example.cab_booking.payload.request.SignupRequest;
import com.example.cab_booking.payload.response.MessageResponse;
import com.example.cab_booking.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /* ---------------------------------------------------------- *
     *  SIGN-IN
     * ---------------------------------------------------------- */
    @PostMapping(
            path = "/signin",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        try {
            String jwt = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(Map.of("token", jwt));   // {"token":"..."}
        }
        catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: Invalid email / username"));
        }
        catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: Invalid password"));
        }
        catch (DisabledException ex) {                        // BLOCKED account
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("Error: Account is blocked"));
        }
    }

    /* ---------------------------------------------------------- *
     *  SIGN-UP
     * ---------------------------------------------------------- */
    @PostMapping(
            path = "/signup",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> registerUser(
            @Valid @RequestBody SignupRequest signUpRequest) {

        if (authService.usernameExists(signUpRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Error: Username is already taken"));
        }
        if (authService.emailExists(signUpRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Error: Email is already in use"));
        }
        if (authService.phoneExists(signUpRequest.getPhone())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Error: Phone number is already in use"));
        }

        MessageResponse msg = authService.registerUser(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }
}
