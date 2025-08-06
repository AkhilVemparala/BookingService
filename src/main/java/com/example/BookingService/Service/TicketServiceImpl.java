package com.example.BookingService.Service;

import com.example.BookingService.Entity.Ticket;
import com.example.BookingService.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public void createTicket(Ticket ticket) {
        ticketRepository.saveAndFlush(ticket);
    }
}
