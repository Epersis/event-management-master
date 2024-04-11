package org.eventmanagement.controller;

import jakarta.validation.Valid;

import java.util.Optional;

import org.eventmanagement.dto.BookingDto;
import org.eventmanagement.dto.MessageResponse;
import org.eventmanagement.exception.BadRequestException;
import org.eventmanagement.exception.EntityDoesNotExistException;
import org.eventmanagement.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;


    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createBooking(@RequestBody @Valid BookingDto bookingDto, BindingResult bindingResult) throws BadRequestException,
            EntityDoesNotExistException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Input JSON is invalid."));
        }
        Optional<BookingDto> savedBookingDto = this.bookingService.bookEventTicket(bookingDto);
        return new ResponseEntity<>(savedBookingDto.get(), HttpStatus.CREATED);
    }

    @PostMapping("/ticket-officer/book/{customerEmail}")
    @PreAuthorize("hasRole('TICKET_OFFICER')")
    public ResponseEntity<?> bookEventTicketForCustomer(@RequestBody @Valid BookingDto bookingDto, @PathVariable String customerEmail, BindingResult bindingResult) throws BadRequestException,
            EntityDoesNotExistException {
    
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Input JSON is invalid."));
        }
        Optional<BookingDto> savedBookingDto = this.bookingService.bookEventTicketForCustomer(bookingDto, customerEmail);
        return new ResponseEntity<>(savedBookingDto.get(), HttpStatus.CREATED);
    }    

    @GetMapping(value = "/{bookingId}")
    @PreAuthorize("hasRole('EVENT_MANAGER') or hasRole('TICKET_OFFICER') or hasRole('CUSTOMER')")
    public ResponseEntity<?> getBookingById(@PathVariable String bookingId) throws BadRequestException,
            EntityDoesNotExistException {
        Optional<BookingDto> savedBookingDto = this.bookingService.getBookingDetails(bookingId);
        return new ResponseEntity<>(savedBookingDto.get(), HttpStatus.CREATED);
    }

    @PostMapping("/{bookingId}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> cancelBookingById(@PathVariable String bookingId) throws BadRequestException,
            EntityDoesNotExistException {
        Optional<BookingDto> savedBookingDto = this.bookingService.cancelBookingById(bookingId);
        return new ResponseEntity<>(savedBookingDto.get(), HttpStatus.CREATED);
    }


}
