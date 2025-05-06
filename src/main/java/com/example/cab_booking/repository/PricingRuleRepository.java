// src/main/java/com/example/cab_booking/repository/PricingRuleRepository.java
package com.example.cab_booking.repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.cab_booking.model.PricingRule;
import com.example.cab_booking.model.VehicleCategory;

@Repository
public interface PricingRuleRepository extends JpaRepository<PricingRule, Long> {

    List<PricingRule> findByVehicleCategoryAndCityAndDayOfWeek(
            VehicleCategory category,
            String          city,
            DayOfWeek       dayOfWeek
    );

    /** Most-specific rule first, the NULL columns are treated as wild-cards. */
    @Query("""
           SELECT r
             FROM PricingRule r
            WHERE (r.vehicleCategory IS NULL OR r.vehicleCategory = :cat)
              AND (r.city            IS NULL OR r.city           = :city)
              AND (r.dayOfWeek       IS NULL OR r.dayOfWeek      = :dow)
              AND (r.timeBand        IS NULL OR
                   :time BETWEEN SUBSTRING(r.timeBand,1,5) AND SUBSTRING(r.timeBand,7,5))
         ORDER BY
               CASE WHEN r.vehicleCategory IS NULL THEN 1 ELSE 0 END ,
               CASE WHEN r.city            IS NULL THEN 1 ELSE 0 END ,
               CASE WHEN r.dayOfWeek       IS NULL THEN 1 ELSE 0 END ,
               CASE WHEN r.timeBand        IS NULL THEN 1 ELSE 0 END
           """)
    Optional<PricingRule> findBestRule(VehicleCategory cat,
                                       String          city,
                                       DayOfWeek       dow,
                                       String          time);       // "HH:mm" 
}
