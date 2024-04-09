package org.eventmanagement.dto;

import org.eventmanagement.enums.TicketState;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class TicketDto {
    private Long id;
    private TicketState ticketState = TicketState.INACTIVE;
    private UUID bookingId; // Add this line

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TicketState getTicketState() {
        return ticketState;
    }

    public void setTicketState(TicketState ticketState) {
        this.ticketState = ticketState;
    }

    public UUID getBookingId() {
        return this.bookingId; // Modify this line
    }

    public void setBookingId(UUID bookingId) { // Add this method
        this.bookingId = bookingId;
    }
}