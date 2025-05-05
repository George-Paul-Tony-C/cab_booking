// src/main/java/com/example/cab_booking/repository/UserRepository.java
package com.example.cab_booking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cab_booking.enums.Role;
import com.example.cab_booking.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /* ---------- look-ups ---------- */
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByPhone(String phone);
    Optional<User> findByUsernameOrEmail(String username, String email);

    /* ---------- existence checks ---------- */
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    /* ---------- role-based fetch ---------- */
    List<User> findByRole(Role role);          // e.g. all DRIVERS
}
