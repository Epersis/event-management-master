package org.eventmanagement.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

import org.eventmanagement.enums.EventType;

public class BookingEventDetailsDto implements Serializable {

    private String name;

    private String venue;

    private Date eventDateTime;

    private EventType eventType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Date getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(Date eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
