package org.eventmanagement.model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.eventmanagement.dto.PaymentMethod;
import org.eventmanagement.dto.TransactionStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "booking-transactions")
public class PaymentTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;

    private double amount;

    private TransactionStatus transactionStatus;

    private PaymentMethod paymentMethod;


    @JsonIgnore
    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="booking_id")
    private Booking booking;

    @Basic
    @CreationTimestamp
    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP")
    private Date transactionDateTime;

    @UpdateTimestamp
    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP ON " +
            "UPDATE CURRENT_TIMESTAMP")
    private Date transactionUpdateDateTime;


    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public Date getTransactionUpdateDateTime() {
        return transactionUpdateDateTime;
    }

    public void setTransactionUpdateDateTime(Date transactionUpdateDateTime) {
        this.transactionUpdateDateTime = transactionUpdateDateTime;
    }
}
