package com.example.BookingService.Model;

import com.example.BookingService.Entity.Passenger;
import lombok.Data;

import java.util.List;

@Data
public class BookingDetails {

    private int pnr;
    private double totalFare;
    private List<Passenger> passengerList;
}

