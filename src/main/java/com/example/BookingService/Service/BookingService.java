package com.example.BookingService.Service;

import com.example.BookingService.Exception.BookingServiceException;
import com.example.BookingService.Model.BookingDetails;
import com.example.BookingService.Model.PassengerDetails;

public interface BookingService {
    BookingDetails bookFlight(String flightId, PassengerDetails passengerDetails, String username)
            throws BookingServiceException;
}
