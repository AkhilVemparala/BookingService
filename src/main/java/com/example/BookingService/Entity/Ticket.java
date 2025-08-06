package com.example.BookingService.Entity;
import java.util.Date;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TICKET_DETAILS")
public class Ticket {

    @Id
    private int pnr;

    @Temporal(TemporalType.DATE)
    private Date bookingDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private Date departureDate;

    private String departureTime;

    private double totalFare;

    private String flightId;

    private String userId;

    private int noOfSeats;

}