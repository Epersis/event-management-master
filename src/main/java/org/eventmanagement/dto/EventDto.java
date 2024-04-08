package org.eventmanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

import org.eventmanagement.enums.EventState;
import org.eventmanagement.enums.EventType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EventDto implements Serializable {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String venue;

    private Date eventDateTime;

    private double ticketPrice;

    @Min(value=1)
    private int totalTickets;

    @Min(value=1)
    private int availableTickets;

    private EventType eventType;

    private EventState eventState = EventState.ACTIVE;

    private String createdBy;

    private Date createdDateTime;

    private Date lastModifiedDateTime;

    private int attendance_count; 


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public EventState getEventState() {
        return eventState;
    }

    public void setEventState(EventState eventState) {
        this.eventState = eventState;
    }

    public Date getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(Date eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(Date lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }


    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketsSold() {
        return totalTickets - availableTickets;
    }

    public int getAttendanceCount() {
        return attendance_count;
    }
    
    public void setAttendanceCount(int attendanceCount) {
        this.attendance_count = attendanceCount;
    }
}
