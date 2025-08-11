package com.example.BookingService.Service;

import com.example.BookingService.Entity.Passenger;
import com.example.BookingService.Entity.Ticket;
import com.example.BookingService.Exception.BookingServiceException;
import com.example.BookingService.FeignClient.FlightServiceFeignClient;
import com.example.BookingService.FeignClient.UserServiceFeignClient;
import com.example.BookingService.Model.BookingDetails;
import com.example.BookingService.Model.Flight;
import com.example.BookingService.Model.LoginDetails;
import com.example.BookingService.Model.PassengerDetails;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private FlightServiceFeignClient flightClient;

    @Autowired
    UserServiceFeignClient userServiceFeignClient;

    @Override
    public BookingDetails bookFlight(String flightId, PassengerDetails passengerDetails, String username)
            throws BookingServiceException {

        log.debug("Passenger list: {}", passengerDetails.getPassengerList());
        if (passengerDetails == null || passengerDetails.getPassengerList() == null || passengerDetails.getPassengerList().isEmpty()) {
            throw new BookingServiceException("Passenger details are missing or empty");
        }

        log.info("Fetching login details for user: {}", username);
        ResponseEntity<LoginDetails> responseEntity = userServiceFeignClient.getUserDetails(username);
        log.info("LoginDetails received {}", responseEntity);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new BookingServiceException("Login service unreachable: " + username);
        }

        LoginDetails response = responseEntity.getBody();
        log.info("Login success for user: {}, session: {}", username, response.isSuccess());

        if (response == null || !response.isSuccess()) {
            throw new BookingServiceException("Session invalid or expired for : " + username);
        }

        Flight flight = getFlightDetails(flightId);
        if (flight == null) {
            throw new BookingServiceException("Flight not found for ID: " + flightId);
        }
        log.info("Flight details fetched: {}", flight);

        int pnr = (int) (Math.random() * 1858955);
        int noOfSeats = passengerDetails.getPassengerList().size();
        double totalFare = flight.getFare() * noOfSeats;

        Ticket ticket = new Ticket();
        ticket.setPnr(pnr);
        ticket.setBookingDate(new Date());
        ticket.setDepartureDate(flight.getFlightAvailableDate());
        ticket.setDepartureTime(flight.getDepartureTime());
        ticket.setFlightId(flight.getFlightId());
        ticket.setUserId(username);
        ticket.setTotalFare(totalFare);
        ticket.setNoOfSeats(noOfSeats);

        log.debug("Ticket object before save: {}", ticket);

        ticketService.createTicket(ticket);
        addPassengers(passengerDetails.getPassengerList(), ticket);
        // TODO: Optionally call flight microservice to reduce available seats

        BookingDetails bookingDetails = new BookingDetails();
        bookingDetails.setPnr(pnr);
        bookingDetails.setTotalFare(totalFare);
        bookingDetails.setPassengerList(passengerDetails.getPassengerList());

        return bookingDetails;
    }

    private Flight getFlightDetails(String flightId) throws BookingServiceException {
        try {
            //ResponseEntity<Flight> response = restTemplate.getForEntity(url, Flight.class);
            ResponseEntity<Flight> response = flightClient.getFlightDetails(flightId);
            return response.getBody();
        } catch (Exception ex) {
            log.error("Error calling flight service: {}", ex.getMessage());
            throw new BookingServiceException("Unable to fetch flight details from Flights microservice.");
        }
    }

    private void addPassengers(List<Passenger> passengers, Ticket ticket) {
        for (Passenger passenger : passengers) {
            passenger.setTicket(ticket);
        }
        passengerService.createPassenger(passengers);
    }
}