package org.eventmanagement.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.eventmanagement.enums.TicketState;

import java.util.UUID;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TicketState ticketState;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    // Getters and setters

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

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        if (this.booking != null && this.booking.equals(booking)) {
            return; // Ticket is already associated with this booking
        }
    
        this.booking = booking;
        if (booking != null) {
            booking.addTicket(this);
        }
    }

    public UUID getBookingId() {
        return this.booking.getBookingId();
    }

}
