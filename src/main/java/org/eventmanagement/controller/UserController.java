package org.eventmanagement.controller;

import java.util.List;

import org.eventmanagement.dto.BookingDto;
import org.eventmanagement.dto.UserDetailsDto;
import org.eventmanagement.exception.EntityDoesNotExistException;
import org.eventmanagement.service.BookingService;
import org.eventmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private BookingService bookingService;


    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() throws EntityDoesNotExistException {
        UserDetailsDto userDetails = this.userService.getUserProfile();
        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }

    //TODO - Implementation
    @GetMapping("/bookings")
    public ResponseEntity<?> getUserBookings() throws EntityDoesNotExistException {
        List<BookingDto> bookings = this.bookingService.getUserBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
}
