package com.example.BookingService.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookingServiceException extends RuntimeException{

    public BookingServiceException(String message) {
        super(message);
    }
}
