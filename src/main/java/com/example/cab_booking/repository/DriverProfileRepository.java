// src/main/java/com/example/cab_booking/repository/DriverProfileRepository.java
package com.example.cab_booking.repository;

import com.example.cab_booking.model.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverProfileRepository extends JpaRepository<DriverProfile, Long> {
    /*  Long  = same PK type as DriverProfile (user_id)  */

    /* --- add custom look-ups if you need them later, e.g. ---
       Optional<DriverProfile> findByLicenseNo(String licenseNo);
    */
}
