package com.example.BookingService.FeignClient;

import com.example.BookingService.Model.LoginDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceFeignClient {

    @GetMapping("/api/user/checkSession/{userId}")
    public ResponseEntity<LoginDetails> getUserDetails(@PathVariable("userId")  String userId);
}