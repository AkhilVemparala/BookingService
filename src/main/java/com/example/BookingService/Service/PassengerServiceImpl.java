package com.example.BookingService.Service;

import java.util.List;

import com.example.BookingService.Entity.Passenger;
import com.example.BookingService.Repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Override
    public void createPassenger(List<Passenger> passengers) {
        passengerRepository.saveAll(passengers);
    }
}

