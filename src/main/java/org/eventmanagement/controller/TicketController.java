package org.eventmanagement.controller;

import org.eventmanagement.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.eventmanagement.dto.EventDto;
import org.eventmanagement.dto.TicketDto;
import org.eventmanagement.enums.EventState;
import org.eventmanagement.enums.TicketState;
import org.eventmanagement.exception.BadRequestException;
import org.eventmanagement.exception.EntityDoesNotExistException;
import org.eventmanagement.controller.EventController;
import org.eventmanagement.repository.TicketRepository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;
import java.util.*;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EventController eventController;

    @GetMapping("/check_validity/{ticketId}")
    @CrossOrigin
    @PreAuthorize("hasRole('TICKET_OFFICER')")
    public ResponseEntity<?> getTicketStatus(@PathVariable long ticketId) {
        // try {
        //     String ticketStatus = ticketService.getTicketStatus(ticketId);
        //     return ResponseEntity.ok(ticketStatus);
        // } catch (EntityDoesNotExistException e) {
        //     return ResponseEntity.notFound().build();
        // }

        try {
            String message = null;
            

            // 1. check for booking validity
            // 2. check for event validity (active, today, ongoing)
            // 3. check for ticket validity (if alr active)

            boolean ticketValidity = (ticketService.getTicketStatus(ticketId) == "INACTIVE");
            boolean isBookingValid = ticketService.isBookingValid(ticketId);
            boolean isEventCompleted = ticketService.isEventCompleted(ticketId);
            boolean isEventCancelled = ticketService.isEventCancelled(ticketId);
            boolean isTicketEventDateToday = ticketService.isTicketEventDateToday(ticketId);
            boolean isTicketEventTimeOverdue = ticketService.isTicketEventTimeOverdue(ticketId);

            if (ticketValidity) {
                message = "Ticket is valid.";
            }

            //check for valid booking 
            else if (!isBookingValid) {
                message = "Booking is not active.";
            }

            // Check if the event is active
            else if (isEventCompleted) {
                message = "Event is over.";
            }

            else if (isEventCancelled) {
                message = "Event has been cancelled.";
            }

            else {
                message = "Ticket has already been admitted.";
            }

            Map<String, Object> response = new HashMap<>();
            response.put("validity", ticketValidity);
            response.put("message", message);
            
            return ResponseEntity.ok(response);

        } catch (EntityDoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admit/{ticketId}") 
    @CrossOrigin
    @PreAuthorize("hasRole('TICKET_OFFICER')")
    public ResponseEntity<?> admitTicket(@PathVariable long ticketId) {
        try {
            StringBuilder errorMessage = new StringBuilder();

            //ticket info
            Optional<TicketDto> ticket = ticketService.getTicketById(ticketId);

            // 1. check for booking validity
            // 2. check for event validity (active, today, ongoing)
            // 3. check for ticket validity (if alr active)

            boolean isBookingValid = ticketService.isBookingValid(ticketId);
            boolean isEventCompleted = ticketService.isEventCompleted(ticketId);
            boolean isEventCancelled = ticketService.isEventCancelled(ticketId);
            boolean isTicketEventDateToday = ticketService.isTicketEventDateToday(ticketId);
            boolean isTicketEventTimeOverdue = ticketService.isTicketEventTimeOverdue(ticketId);

            //check for valid booking 
            if (!isBookingValid) {
                errorMessage.append("Booking is not active.");
            }

            // Check if the event is active
            else if (isEventCompleted) {
                errorMessage.append("Event is over.");
            }

            else if (isEventCancelled) {
                errorMessage.append("Event has been cancelled.");
            }

            // Check if the event's date of the ticket is today
            else if (!isTicketEventDateToday) {
                errorMessage.append("Event is not open for admission.");
            }

            // Check if the event has already begun
            else if (!isTicketEventTimeOverdue) {
                errorMessage.append("Event has already begun.");
            }            

            if (errorMessage.length() > 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("errorMessage", errorMessage);
                response.put("ticket", ticket); 
                return ResponseEntity.badRequest().body(response);
            }

            // If there are no errors, admit the ticket
            // Only a ticket that is INACTIVE can be admitted
            ticketService.changeTicketState(ticketId, TicketState.ACTIVE);
            // return ResponseEntity
            //         .ok("Ticket with id " + ticketId + " has been admitted. Ticket status: " + TicketState.ACTIVE);
            
            return ResponseEntity.ok(ticket);

        } catch (EntityDoesNotExistException e) {
            Optional<TicketDto> ticket = ticketService.getTicketById(ticketId);
            return new ResponseEntity<>(ticket.get(), HttpStatus.NOT_FOUND);
            // return ResponseEntity.notFound().build();
        } catch (BadRequestException e) {
            Optional<TicketDto> ticket = ticketService.getTicketById(ticketId);
            Map<String, Object> response = new HashMap<>();
            response.put("errorMessage", e.getMessage());
            response.put("ticket", ticket); 
            return ResponseEntity.badRequest().body(response);

            // return new ResponseEntity<>(ticket.get(), HttpStatus.BAD_REQUEST);
            // return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            Optional<TicketDto> ticket = ticketService.getTicketById(ticketId);
            return new ResponseEntity<>(ticket.get(), HttpStatus.INTERNAL_SERVER_ERROR);
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by_booking/{bookingId}")
    @PreAuthorize("hasRole('TICKET_OFFICER') or hasRole('CUSTOMER')")
    public ResponseEntity<?> getTicketsByBookingId(@PathVariable UUID bookingId) {
        List<TicketDto> tickets = ticketService.getTicketsByBookingId(bookingId);
        return ResponseEntity.ok(tickets);
    }
}