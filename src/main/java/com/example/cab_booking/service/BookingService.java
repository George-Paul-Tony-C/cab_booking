// src/main/java/com/example/cab_booking/service/BookingService.java
package com.example.cab_booking.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.cab_booking.enums.BookingStatus;
import com.example.cab_booking.enums.PaymentStatus;
import com.example.cab_booking.model.Booking;
import com.example.cab_booking.model.Place;
import com.example.cab_booking.model.PricingRule;
import com.example.cab_booking.model.Route;
import com.example.cab_booking.model.Trip;
import com.example.cab_booking.model.User;
import com.example.cab_booking.model.Vehicle;
import com.example.cab_booking.model.VehicleCategory;
import com.example.cab_booking.payload.request.BookingRequest;
import com.example.cab_booking.payload.response.BookingResponse;
import com.example.cab_booking.repository.BookingRepository;
import com.example.cab_booking.repository.PlaceRepository;
import com.example.cab_booking.repository.PricingRuleRepository;
import com.example.cab_booking.repository.RouteRepository;
import com.example.cab_booking.repository.TripRepository;
import com.example.cab_booking.repository.UserRepository;
import com.example.cab_booking.repository.VehicleCategoryRepository;
import com.example.cab_booking.repository.VehicleRepository;
import com.example.cab_booking.security.services.UserDetailsImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository         bookingRepo;
    private final RouteRepository           routeRepo;
    private final PlaceRepository           placeRepo;
    private final VehicleCategoryRepository catRepo;
    private final PricingRuleRepository     ruleRepo;
    private final UserRepository            userRepo;
    private final TripRepository            tripRepo;
    private final VehicleRepository         vehicleRepo;
    private final NotificationService       notify;
    private final PriceService              priceService;

    /* ---------- helpers ---------- */
    private User currentUser() {
        Object pr = SecurityContextHolder.getContext()
                                         .getAuthentication()
                                         .getPrincipal();
        if (!(pr instanceof UserDetailsImpl p))
            throw new IllegalStateException("No authenticated user");
        return userRepo.findById(p.getId())
                       .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    private static BookingResponse toDto(Booking b) {
        return BookingResponse.builder()
                .id(b.getId())
                .status(b.getBookingStatus())
                .otp(b.getOtp())
                .fromPlace(b.getRoute().getFromPlace().getCity())
                .toPlace(b.getRoute().getToPlace().getCity())
                .distanceKm(b.getRoute().getDistanceKm())
                .vehicleCategory(b.getVehicleCategory().getName())
                .pickupTime(b.getPickupTimeRequested())
                .driverId(b.getDriver() != null ? b.getDriver().getId() : null)
                .build();
    }

    /* =============================================================
     * CUSTOMER
     * ============================================================= */

    public BookingResponse createBooking(BookingRequest req) {

        User customer = currentUser();

        Place from = placeRepo.findById(req.getFromPlaceId())
                              .orElseThrow(() -> new IllegalArgumentException("Invalid origin"));
        Place to   = placeRepo.findById(req.getToPlaceId())
                              .orElseThrow(() -> new IllegalArgumentException("Invalid destination"));

        VehicleCategory cat = catRepo.findById(req.getVehicleCategoryId())
                                     .orElseThrow(() -> new IllegalArgumentException("Invalid category"));

        Route route = routeRepo
                .findByFromPlaceAndToPlaceAndVehicleCategory(from, to, cat)
                .orElseThrow(() -> new IllegalArgumentException("No route for selection"));

        /* -------- fare estimate -------- */
        LocalDateTime now = LocalDateTime.now();
        PricingRule rule  = ruleRepo.findBestRule(cat,
                                                 from.getCity(),
                                                 now.getDayOfWeek(),
                                                 now.toLocalTime().toString().substring(0,5))
                                    .orElse(null);

        Booking booking = Booking.builder()
                .customer(customer)
                .route(route)
                .vehicleCategory(cat)
                .bookingStatus(BookingStatus.WAITING)
                .pickupTimeRequested(req.getPickupTime() == null ? now : req.getPickupTime())
                .build();

        bookingRepo.save(booking);

        /* ---- notifications ---- */
        notify.push(customer,
                "Booking #" + booking.getId() + " created. Waiting for driver...",
                "push");

        vehicleRepo.findByCurrentPlace_IdAndAvailableTrue(from.getId())
                   .stream()
                   .map(Vehicle::getDriver)
                   .distinct()
                   .forEach(d -> notify.push(d,
                           "New booking at " + from.getLandmark() +
                                   " (" + cat.getName() + ")",
                           "push"));

        return toDto(booking);
    }

    public List<BookingResponse> getMyBookings() {
        return bookingRepo.findByCustomer_Id(currentUser().getId())
                          .stream()
                          .map(BookingService::toDto)
                          .collect(Collectors.toList());
    }

    public void cancelBooking(Long id) {
        Booking b = bookingRepo.findById(id).orElseThrow();
        if (!b.getCustomer().getId().equals(currentUser().getId()))
            throw new IllegalStateException("Not your booking");
        b.setBookingStatus(BookingStatus.CANCELLED);
        b.setCancelledBy("USER");
    }


        // Getting Booking By Id
        public BookingResponse getBookingById(Long id) {
                Booking booking = bookingRepo.findById(id)
                                                                         .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
                if (!booking.getCustomer().getId().equals(currentUser().getId()))
                        throw new IllegalStateException("Not your booking");
                return toDto(booking);
        }

    /* =============================================================
     * DRIVER – queue & accept
     * ============================================================= */

    private Long currentDriverPlaceId(User driver) {
        return vehicleRepo.findFirstByDriver_IdAndAvailableTrue(driver.getId())
                          .map(v -> v.getCurrentPlace().getId())
                          .orElseThrow(() -> new IllegalStateException(
                                  "Driver has no available vehicle with a location"));
    }

    public List<BookingResponse> waitingForCurrentDriver() {
        User driver  = currentUser();
        Long placeId = currentDriverPlaceId(driver);

        return bookingRepo.findByRoute_FromPlace_IdAndBookingStatus(
                                  placeId, BookingStatus.WAITING)
                          .stream()
                          .map(BookingService::toDto)
                          .collect(Collectors.toList());
    }

    public BookingResponse driverAccept(Long bookingId) {
        User driver = currentUser();
        Booking b   = bookingRepo.findById(bookingId).orElseThrow();

        if (b.getBookingStatus() != BookingStatus.WAITING)
            throw new IllegalStateException("Booking already taken");

        b.setBookingStatus(BookingStatus.ACCEPTED);
        b.setDriver(driver);
        b.setOtp(String.valueOf(1000 + new Random().nextInt(9000)));   // 4-digit

        notify.push(b.getCustomer(),
                "Driver " + driver.getUsername() +
                        " accepted booking #" + b.getId() +
                        ". Your OTP: " + b.getOtp(),
                "SMS");

        notify.push(driver,
                "You accepted booking #" + b.getId() + ". Pick-up OTP: " + b.getOtp(),
                "push");

        return toDto(b);
    }

    /* =============================================================
     * DRIVER – start
     * ============================================================= */

    public BookingResponse startTrip(Long bookingId, String otp) {

        User driver = currentUser();
        Booking b   = bookingRepo.findById(bookingId).orElseThrow();

        if (!driver.getId().equals(b.getDriver().getId()))
            throw new IllegalStateException("Not your booking");
        if (b.getBookingStatus() != BookingStatus.ACCEPTED)
            throw new IllegalStateException("Booking not accepted yet");
        if (!b.getOtp().equals(otp))
            throw new IllegalStateException("Invalid OTP");

        /* prevent duplicate rows */
        if (tripRepo.existsByBooking_Id(bookingId))
            throw new IllegalStateException("Trip already started");

        b.setBookingStatus(BookingStatus.STARTED);

        Vehicle v = vehicleRepo.findByDriverId(driver.getId()).stream()
                               .filter(veh -> veh.getCategory().getId()
                                       .equals(b.getVehicleCategory().getId()))
                               .findFirst()
                               .orElseThrow(() -> new IllegalStateException("Driver vehicle not found"));
        v.setAvailable(false);

        Trip trip = Trip.builder()
                .booking(b)
                .driver(driver)
                .vehicle(v)
                .route(b.getRoute())
                .pickupPlace(b.getRoute().getFromPlace())
                .startTime(LocalDateTime.now())
                .surgeMultiplier(1.0)
                .build();
        tripRepo.save(trip);

        notify.push(b.getCustomer(),
                "Ride started. Enjoy your trip!",
                "push");

        return toDto(b);
    }

    /* =============================================================
     * DRIVER – complete
     * ============================================================= */

     public BookingResponse completeTrip(Long bookingId) {

        User driver = currentUser();
        Booking b   = bookingRepo.findById(bookingId)
                                 .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    
        if (!driver.getId().equals(b.getDriver().getId()))
            throw new IllegalStateException("Not your booking");
        if (b.getBookingStatus() != BookingStatus.STARTED)
            throw new IllegalStateException("Trip not in progress");
    
        Trip trip = tripRepo.findTopByBooking_IdOrderByIdAsc(bookingId)
                            .orElseThrow(() -> new IllegalStateException("Trip row missing"));
    
        /* ---------- real-world distance ---------- */
        double distanceKm = b.getRoute().getDistanceKm();   // or pull from GPS/odometer later
    
        /* ---------- look up pricing rule ---------- */
        PricingRule rule = ruleRepo.findBestRule(
                b.getVehicleCategory(),
                trip.getPickupPlace().getCity(),
                LocalDateTime.now().getDayOfWeek(),
                LocalDateTime.now().toLocalTime().toString().substring(0,5))
            .orElse(null);
    
        /* ---------- single source of truth ---------- */
        double fare = priceService.computeFare(
                b.getVehicleCategory(), distanceKm, rule);
    
        /* ---------- update trip / booking ---------- */
        trip.setEndTime(LocalDateTime.now());
        trip.setDropPlace(b.getRoute().getToPlace());
        trip.setDistanceActualKm(distanceKm);
        trip.setSurgeMultiplier(rule == null ? 1.0 : rule.getSurgeMultiplier());
        trip.setFareFinal(fare);
        trip.setPaymentStatus(PaymentStatus.PAID);
    
        b.setBookingStatus(BookingStatus.COMPLETED);
    
        Vehicle v = trip.getVehicle();
        v.setAvailable(true);
        v.setCurrentPlace(trip.getDropPlace());
    
        notify.push(b.getCustomer(),
                "Trip completed ✔ Fare ₹" + fare + " paid in cash.",
                "push");
    
        return toDto(b);
    }
    
}
