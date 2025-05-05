// src/main/java/com/example/cab_booking/model/User.java
package com.example.cab_booking.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.cab_booking.enums.Role;
import com.example.cab_booking.enums.UserStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@ToString(exclude = {           // avoid recursive logging
        "driverProfile",
        "vehicles",
        "customerBookings",
        "driverTrips",
        "notifications",
        "reviewsAuthored",
        "reviewsReceived"
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;                   // hashed!

    @Column(nullable = false, length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private UserStatus status = UserStatus.ACTIVE;

    private String profilePictureUrl;
    private LocalDateTime lastLoginAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /* ----------------------------------------------------------------
       Inverse-side relationships
       ---------------------------------------------------------------- */

    // 1️⃣ DRIVER_PROFILE (One-to-One)
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY,
              cascade = CascadeType.ALL, orphanRemoval = true)
    private DriverProfile driverProfile;

    // 2️⃣ VEHICLES this user drives
    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private List<Vehicle> vehicles;

    // 3️⃣ BOOKINGS made by this user as CUSTOMER
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Booking> customerBookings;

    // 4️⃣ TRIPS driven by this user (if role = DRIVER)
    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private List<Trip> driverTrips;

    // 5️⃣ NOTIFICATIONS sent to this user
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
               cascade = CascadeType.ALL)
    private List<Notification> notifications;

    // 6️⃣ REVIEWS authored by the user (as customer)
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Review> reviewsAuthored;

    // 7️⃣ REVIEWS received by the user (as driver)
    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private List<Review> reviewsReceived;
}
