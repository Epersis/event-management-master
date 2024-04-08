package org.eventmanagement.dto;

import org.eventmanagement.enums.TicketState;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketDto {
    private Long id;

    // Set the default state to INACTIVE
    private TicketState ticketState = TicketState.INACTIVE;

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
}
