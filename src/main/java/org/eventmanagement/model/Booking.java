package org.eventmanagement.model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.eventmanagement.dto.PaymentMethod;
import org.eventmanagement.enums.BookingStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "bookings")
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bookingId;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PaymentTransaction transactionDetail;

    private int numberOfTickets;

    @Basic
    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date bookingDateTime;

    private Date bookingUpdateTime;

    private String bookingUserEmail;

    private Long bookingMobileNumber;

    private String bookedBy;

    private BookingStatus bookingStatus;

    private PaymentMethod paymentMethod;

    private long eventId;

    @Basic
    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP")
    private Date createdDateTime;

    @UpdateTimestamp
    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP ON " +
            "UPDATE CURRENT_TIMESTAMP")
    private Date lastModifiedDateTime;


    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public PaymentTransaction getTransactionDetail() {
        return transactionDetail;
    }

    public void setTransactionDetail(PaymentTransaction transactionDetail) {
        this.transactionDetail = transactionDetail;
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

}


