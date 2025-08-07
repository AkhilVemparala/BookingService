package com.example.BookingService.Controller;

import com.example.BookingService.Exception.BookingServiceException;
import com.example.BookingService.Model.BookingDetails;
import com.example.BookingService.Model.PassengerDetails;
import com.example.BookingService.Service.BookingService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/book")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    Logger logger = org.slf4j.LoggerFactory.getLogger(BookingController.class);

    @PostMapping(value = "/{flightId}/{username}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> bookFlight(@PathVariable("flightId") String flightId,
                                        @Valid @RequestBody PassengerDetails passengerDetails,
                                        @PathVariable("username") String username,
                                        Errors errors) throws BookingServiceException {
        logger.info("Booking request received for flightId: {}, username: {}", flightId, username);

        if (errors.hasErrors()) {
            return new ResponseEntity<>(
                    new BookingServiceException("Validation errors occurred: " + errors.getAllErrors()),
                    HttpStatus.BAD_REQUEST
            );
        }
        logger.info("Booking successful for flightId: {}, username: {}", flightId, username);
        BookingDetails bookingDetails = bookingService.bookFlight(flightId, passengerDetails, username);
        return new ResponseEntity<>(bookingDetails, HttpStatus.OK);
    }
}