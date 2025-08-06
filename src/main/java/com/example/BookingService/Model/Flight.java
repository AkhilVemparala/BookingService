package com.example.BookingService.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor// generates getters, setters, toString, equals, hashCode
public class Flight {
    @Id
    private String flightId;
    private String source;
    private String destination;
    private String airlines;
    private double fare;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date flightAvailableDate;

//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private LocalDate flightAvailableDate;

    private Integer seatCount;
    private String departureTime;
    private String arrivalTime;
}

