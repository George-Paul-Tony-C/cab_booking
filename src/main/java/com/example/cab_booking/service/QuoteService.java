// src/main/java/com/example/cab_booking/service/QuoteService.java
package com.example.cab_booking.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cab_booking.model.Place;
import com.example.cab_booking.model.PricingRule;
import com.example.cab_booking.model.Route;
import com.example.cab_booking.model.VehicleCategory;
import com.example.cab_booking.payload.request.QuoteRequest;
import com.example.cab_booking.payload.response.QuoteItem;
import com.example.cab_booking.payload.response.QuoteResponse;
import com.example.cab_booking.repository.PlaceRepository;
import com.example.cab_booking.repository.PricingRuleRepository;
import com.example.cab_booking.repository.RouteRepository;
import com.example.cab_booking.repository.VehicleCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuoteService {

    private final PlaceRepository           placeRepo;
    private final VehicleCategoryRepository catRepo;
    private final RouteRepository           routeRepo;
    private final PricingRuleRepository     ruleRepo;

    public QuoteResponse getQuote(QuoteRequest req) {

        Place from = placeRepo.findById(req.getFromPlaceId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid fromPlaceId"));
        Place to = placeRepo.findById(req.getToPlaceId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid toPlaceId"));

        List<VehicleCategory> categories = (req.getVehicleCategoryIds() == null || req.getVehicleCategoryIds().isEmpty())
                ? catRepo.findAll()
                : catRepo.findAllById(req.getVehicleCategoryIds());

        LocalDateTime now   = LocalDateTime.now();
        DayOfWeek     today = now.getDayOfWeek();

        List<QuoteItem> items = categories.stream().map(cat -> {

            Route route = routeRepo
                    .findByFromPlaceAndToPlaceAndVehicleCategory(from, to, cat)
                    .orElseThrow(() ->
                            new IllegalStateException("No route for category " + cat.getName()));

            double surge = ruleRepo
                    .findByVehicleCategoryAndCityAndDayOfWeek(cat, from.getCity(), today)
                    .stream()
                    .filter(r -> inTimeBand(now.toLocalTime(), r.getTimeBand()))
                    .map(PricingRule::getSurgeMultiplier)
                    .findFirst()
                    .orElse(1.0);

            double base = route.getBasePrice()            // route base
                         + cat.getBasePrice()             // category flag-fall
                         + (route.getDistanceKm() * cat.getPricePerKm())
                         + (route.getTollsEstimate() == null ? 0 : route.getTollsEstimate());

            double price = Math.round(base * surge);

            return QuoteItem.builder()
                    .vehicleCategoryId(cat.getId())
                    .vehicleCategory(cat.getName())
                    .distanceKm(route.getDistanceKm())
                    .surgeMultiplier(surge)
                    .price(price)
                    .build();
        }).collect(Collectors.toList());

        return QuoteResponse.builder()
                .from(from.getCity())
                .to(to.getCity())
                .quotes(items)
                .build();
    }

    /* helper: check if current time within "HH:mm-HH:mm" band */
    private boolean inTimeBand(java.time.LocalTime now, String band) {
        if (band == null || band.isBlank()) return true;
        String[] parts = band.split("-");
        java.time.LocalTime start = java.time.LocalTime.parse(parts[0]);
        java.time.LocalTime end   = java.time.LocalTime.parse(parts[1]);
        return !now.isBefore(start) && !now.isAfter(end);
    }
}
