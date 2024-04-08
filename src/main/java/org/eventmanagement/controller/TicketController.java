package org.eventmanagement.controller;

import org.eventmanagement.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.eventmanagement.dto.EventDto;
import org.eventmanagement.enums.EventState;
import org.eventmanagement.enums.TicketState;
import org.eventmanagement.exception.BadRequestException;
import org.eventmanagement.exception.EntityDoesNotExistException;
import org.eventmanagement.controller.EventController;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EventController eventController;

    @GetMapping("/get_status/{ticketId}")
    @PreAuthorize("hasRole('TICKET_OFFICER')")
    public ResponseEntity<String> getTicketStatus(@PathVariable long ticketId) {
        try {
            String ticketStatus = ticketService.getTicketStatus(ticketId);
            return ResponseEntity.ok(ticketStatus);
        } catch (EntityDoesNotExistException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // changes the state of a ticket to ACTIVE to denote admission of the ticket
    @PutMapping("/admit/{ticketId}")
    @PreAuthorize("hasRole('TICKET_OFFICER')")
    public ResponseEntity<?> admitTicket(@PathVariable long ticketId) {
        try {
            ticketService.changeTicketState(ticketId, TicketState.ACTIVE);
            return ResponseEntity.ok().build();
        } catch (EntityDoesNotExistException e) {
            return ResponseEntity.notFound().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    



}