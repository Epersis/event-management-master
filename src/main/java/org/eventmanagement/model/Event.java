package org.eventmanagement.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

import org.eventmanagement.enums.EventState;
import org.eventmanagement.enums.EventType;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import javax.persistence.Transient;

@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String venue;
    private Date eventDateTime;

    private double ticketPrice;
    private int totalTickets;
    private int availableTickets;
    private EventType eventType;

    private EventState eventState;

    private String createdBy;
    private int attendance_count; 
    private double cancellationFee;
    private double cancellationFeeRevenue;

    @Basic
    @Generated(GenerationTime.INSERT)
    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP")
    private Date createdDateTime;
    @Generated(GenerationTime.ALWAYS)
    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP ON " +
            "UPDATE CURRENT_TIMESTAMP")
    private Date lastModifiedDateTime;


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

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
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

    public EventState getEventState() {
        return eventState;
    }

    public void setEventState(EventState eventState) {
        this.eventState = eventState;
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

    public double getCancellationFee() {
        return this.cancellationFee;
    }

    public void setCancellationFee(double fee) {
        this.cancellationFee = fee;
    }

    public double getCancellationFeeRevenue() {
        return this.cancellationFeeRevenue;
    }

    public void setCancellationFeeRevenue(double amt) {
        this.cancellationFeeRevenue = amt;
    }
    
}
