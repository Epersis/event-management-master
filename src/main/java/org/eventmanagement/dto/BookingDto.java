package org.eventmanagement.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.eventmanagement.enums.BookingStatus;
import org.springframework.format.annotation.DateTimeFormat;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDto implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID bookingId;

    private String bookingUserEmail;

    private Long bookingMobileNumber;

    private String bookedBy;

    private BookingStatus bookingStatus;

    private long eventId;

    @Min(value = 1)
    @Max(value = 4)
    private int numberOfTickets;

    private Date bookingDateTime;

    private Date bookingUpdateTime;

    private BookingEventDetailsDto eventDetails;

    private PaymentTransactionDto transactionDetail;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date createdDateTime;

    private Date lastModifiedDateTime;

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingUserEmail() {
        return bookingUserEmail;
    }

    public void setBookingUserEmail(String bookingUserEmail) {
        this.bookingUserEmail = bookingUserEmail;
    }

    public Long getBookingMobileNumber() {
        return bookingMobileNumber;
    }

    public void setBookingMobileNumber(Long bookingMobileNumber) {
        this.bookingMobileNumber = bookingMobileNumber;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }


    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public Date getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(Date bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public Date getBookingUpdateTime() {
        return bookingUpdateTime;
    }

    public void setBookingUpdateTime(Date bookingUpdateTime) {
        this.bookingUpdateTime = bookingUpdateTime;
    }

    public BookingEventDetailsDto getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(BookingEventDetailsDto eventDetails) {
        this.eventDetails = eventDetails;
    }

    public PaymentTransactionDto getTransactionDetail() {
        return transactionDetail;
    }

    public void setTransactionDetail(PaymentTransactionDto transactionDetail) {
        this.transactionDetail = transactionDetail;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
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
}
