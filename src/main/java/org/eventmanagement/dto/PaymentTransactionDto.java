package org.eventmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class PaymentTransactionDto implements Serializable {

    private UUID transactionId;

    private double amount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private TransactionStatus transactionStatus;

    @NotNull
    private PaymentMethod paymentMethod;

    private Date transactionDateTime;

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
