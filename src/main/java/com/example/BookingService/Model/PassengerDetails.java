package com.example.BookingService.Model;

import com.example.BookingService.Entity.Passenger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDetails {

    @NotEmpty(message = "Passenger list cannot be empty for booking!")
    @Valid
    private List<Passenger> passengerList;
}

