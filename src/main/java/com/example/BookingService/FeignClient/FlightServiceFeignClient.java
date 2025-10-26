package com.example.BookingService.FeignClient;

import com.example.BookingService.Model.Flight;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "FLIGHTSERVICE")
public interface FlightServiceFeignClient {

    @GetMapping("/api/flights/{flightId}")
    ResponseEntity<Flight> getFlightDetails(@PathVariable("flightId") String flightId);
}