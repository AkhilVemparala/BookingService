package com.example.BookingService.Service;

import com.example.BookingService.Entity.Passenger;
import com.example.BookingService.Entity.Ticket;
import com.example.BookingService.Exception.BookingServiceException;
import com.example.BookingService.FeignClient.UserServiceFeignClient;
import com.example.BookingService.Model.BookingDetails;
import com.example.BookingService.Model.Flight;
import com.example.BookingService.Model.LoginDetails;
import com.example.BookingService.Model.PassengerDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;


@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    UserServiceFeignClient userServiceFeignClient;

    private static final String FLIGHT_API_BASE = "http://localhost:8087/api/flights/";

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);


    @Override
    public BookingDetails bookFlight(String flightId, PassengerDetails passengerDetails, String username)
            throws BookingServiceException {

        logger.info("PassengerDetails received {}", passengerDetails );

        if (passengerDetails.getPassengerList().isEmpty()) {
            throw new BookingServiceException("PASSENGER_LIST_EMPTY");
        }

        ResponseEntity<LoginDetails> responseEntity = userServiceFeignClient.getUserDetails(username);
        logger.info("LoginDetails received {}", responseEntity );

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new BookingServiceException("Failed to get login details: " + username);
        }

        LoginDetails response = responseEntity.getBody();
        logger.info("LoginDetails received {}", response );

        if (response == null || !response.isSuccess()) {
            throw new BookingServiceException("Active session not found for user: " + username);
        }

        Flight flight = getFlightDetails(flightId);

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
        String url = FLIGHT_API_BASE + flightId;
        try {
            ResponseEntity<Flight> response = restTemplate.getForEntity(url, Flight.class);
            return response.getBody();
        } catch (BookingServiceException ex) {
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
