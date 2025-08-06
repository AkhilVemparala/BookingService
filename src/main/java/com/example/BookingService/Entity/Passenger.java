package com.example.BookingService.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PASSENGER_DETAILS")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int passengerId;

    private String passengerName;
    private String passengerAge;
    private String passengerGender;

    @ManyToOne
    @JoinColumn(name = "pnr", nullable = false)
    private Ticket ticket;

}