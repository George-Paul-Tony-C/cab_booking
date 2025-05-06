// src/main/java/com/example/cab_booking/service/PriceService.java
package com.example.cab_booking.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cab_booking.model.Place;
import com.example.cab_booking.model.PricingRule;
import com.example.cab_booking.model.Route;
import com.example.cab_booking.model.VehicleCategory;
import com.example.cab_booking.payload.response.PriceQuote;
import com.example.cab_booking.payload.response.PriceQuoteResponse;
import com.example.cab_booking.repository.PlaceRepository;
import com.example.cab_booking.repository.PricingRuleRepository;
import com.example.cab_booking.repository.RouteRepository;
import com.example.cab_booking.repository.VehicleCategoryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Pure stateless fare-calculation logic.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PriceService {

    private final PlaceRepository           placeRepo;
    private final VehicleCategoryRepository catRepo;
    private final RouteRepository           routeRepo;
    private final PricingRuleRepository     ruleRepo;

    /* --------------------------------------------------------------- */

    /**
     * Produce one quote per available VehicleCategory
     * and sort by price (default) or alphabetically.
     */
    public List<PriceQuoteResponse> quote(Long fromId,
                                          Long toId,
                                          String sort) {

        Place from = placeRepo.findById(fromId)
                              .orElseThrow(() -> new IllegalArgumentException("Invalid fromId"));
        Place to   = placeRepo.findById(toId)
                              .orElseThrow(() -> new IllegalArgumentException("Invalid toId"));

        List<Route> routes = routeRepo.findByFromPlaceAndToPlace(from, to);

        LocalDateTime now  = LocalDateTime.now();
        DayOfWeek dow      = now.getDayOfWeek();
        String timeStr     = now.toLocalTime()
                                .format(DateTimeFormatter.ofPattern("HH:mm"));
        String city        = from.getCity();

        List<PriceQuoteResponse> quotes = routes.stream()
                .map(r -> buildQuote(r, city, dow, timeStr))
                .collect(Collectors.toList());

        /* ---------------- sort ---------------- */
        Comparator<PriceQuoteResponse> cmp;
        if ("category".equalsIgnoreCase(sort)) {
            cmp = Comparator.comparing(PriceQuoteResponse::getVehicleCategoryName);
        } else {                                 // default: cheapest first
            cmp = Comparator.comparing(PriceQuoteResponse::getTotal);
        }
        quotes.sort(cmp);
        return quotes;
    }

    /* =============================================================== */

    private PriceQuoteResponse buildQuote(Route route,
                                          String city,
                                          DayOfWeek dow,
                                          String timeHHmm) {

        VehicleCategory cat = route.getVehicleCategory();

        /* ---- step-1 basic fare ---- */
        double base = route.getBasePrice()
                     + route.getDistanceKm() * cat.getPricePerKm();

        /* ---- step-2 pricing rule (optional) ---- */
        PricingRule rule = ruleRepo.findBestRule(cat, city, dow, timeHHmm)
                                   .orElse(null);

        double surge       = rule != null ? rule.getSurgeMultiplier() : 1.0;
        double bookingFee  = rule != null ? rule.getBookingFee()     : 0.0;
        double minFare     = rule != null ? rule.getMinFare()        : 0.0;

        /* ---- step-3 final fare ---- */
        double fare = Math.max(base * surge + bookingFee, minFare);

        return PriceQuoteResponse.builder()
                .fromPlaceId(route.getFromPlace().getId())
                .toPlaceId(route.getToPlace().getId())
                .vehicleCategoryId(cat.getId())
                .vehicleCategoryName(cat.getName())
                .distanceKm(route.getDistanceKm())
                .etaMin(route.getEstimatedTimeMin())
                .baseFare(round2(base))
                .surgeMultiplier(surge)
                .bookingFee(bookingFee)
                .total(round2(fare))
                .build();
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    // src/main/java/com/example/cab_booking/service/PriceService.java
    public List<PriceQuote> quoteAll(Long fromId, Long toId, LocalDateTime when) {

        Place from = placeRepo.findById(fromId).orElseThrow();
        Place to   = placeRepo.findById(toId).orElseThrow();

        DayOfWeek dow      = when.getDayOfWeek();
        String    hhmm     = when.toLocalTime().toString().substring(0,5);   // "HH:mm"

        return catRepo.findAll()                       // every vehicle type
                .stream()
                .map(cat -> {                          // 1️⃣ find route, 2️⃣ rule, 3️⃣ calc
                    Route r = routeRepo.findByFromPlaceAndToPlaceAndVehicleCategory(from, to, cat)
                                        .orElseThrow();
                    PricingRule rule = ruleRepo
                            .findBestRule(cat, from.getCity(), dow, hhmm)
                            .orElse(null);             // null → no surge / booking fee

                    double surge   = rule == null ? 1.0 : rule.getSurgeMultiplier();
                    double booking = rule == null ? 0.0 : rule.getBookingFee();

                    double fare = (r.getBasePrice() + booking) * surge;

                    return new PriceQuote(cat.getId(),
                                        cat.getName(),
                                        fare,
                                        surge,
                                        booking);
                })
                .toList();
    }

    public double computeFare(VehicleCategory cat,
                          double distanceKm,
                          PricingRule rule) {

    /* base fare built from *actual* km */
    double base = cat.getBasePrice() + distanceKm * cat.getPricePerKm();

    double surge      = rule == null ? 1.0 : rule.getSurgeMultiplier();
    double bookingFee = rule == null ? 0.0 : rule.getBookingFee();
    double minFare    = rule == null ? 0.0 : rule.getMinFare();

    return round2(Math.max(base * surge + bookingFee, minFare));
}


}
