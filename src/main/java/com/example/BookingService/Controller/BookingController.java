package com.example.BookingService.Controller;

import com.example.BookingService.Exception.BookingServiceException;
import com.example.BookingService.Model.BookingDetails;
import com.example.BookingService.Model.PassengerDetails;
import com.example.BookingService.Service.BookingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/book")
public class BookingController {

    @Autowired
    private BookingService bookingService;


    @PostMapping(value = "/{flightId}/{username}", produces = "application/json", consumes = "application/json")
    //@CircuitBreaker(name = "bookingServiceCircuitBreaker", fallbackMethod = "fallbackBookFlight")
    //@Retry(name = "bookingServiceRetry", fallbackMethod = "fallbackBookFlight")
    @RateLimiter(name = "bookingServiceRateLimiter", fallbackMethod = "fallbackBookFlight")
    public ResponseEntity<?> bookFlight(@PathVariable("flightId") String flightId,
                                        @Valid @RequestBody PassengerDetails passengerDetails,
                                        @PathVariable("username") String username,
                                        Errors errors) throws BookingServiceException {
        log.info("Booking request received for flightId: {}, username: {}", flightId, username);

        if (errors.hasErrors()) {
            return new ResponseEntity<>(
                    new BookingServiceException("Validation errors occurred: " + errors.getAllErrors()),
                    HttpStatus.BAD_REQUEST
            );
        }
        log.info("Booking successful for flightId: {}, username: {}", flightId, username);
        BookingDetails bookingDetails = bookingService.bookFlight(flightId, passengerDetails, username);
        return new ResponseEntity<>(bookingDetails, HttpStatus.OK);
    }
    int retryCount = 0;

    public ResponseEntity<?> fallbackBookFlight(String flightId, PassengerDetails passengerDetails, String username, Errors errors, Throwable throwable) {
        log.error("Fallback method called for booking flightId: {}, username: {}, error: {}", flightId, username, throwable.getMessage());
//        return new ResponseEntity<>(
//                new BookingServiceException("Booking service is currently unavailable. Please try again later."),
//                HttpStatus.SERVICE_UNAVAILABLE
//        );
        retryCount++;
        log.error("Retry count {}", retryCount);


        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Booking service is currently unavailable. Please try again later.");
    }
}