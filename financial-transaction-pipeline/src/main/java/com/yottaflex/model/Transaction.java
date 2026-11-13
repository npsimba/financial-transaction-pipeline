
package com.yottaflex.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private String legId;
    private String eventId;
    private String transactionId;
    private String accountId;
    private String legType;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime eventTimestamp;
    private int eventSequence;

    // Default constructor
    public Transaction() {
    }

    // Constructor with all fields
    public Transaction(String legId, String eventId, String transactionId,
                       String accountId, String legType, BigDecimal amount,
                       String currency, LocalDateTime eventTimestamp,
                       int eventSequence) {

        this.legId = legId;
        this.eventId = eventId;
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.legType = legType;
        this.amount = amount;
        this.currency = currency;
        this.eventTimestamp = eventTimestamp;
        this.eventSequence = eventSequence;
    }

    public String getLegId() {
        return legId;
    }

    public void setLegId(String legId) {
        this.legId = legId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getLegType() {
        return legType;
    }

    public void setLegType(String legType) {
        this.legType = legType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(LocalDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public int getEventSequence() {
        return eventSequence;
    }

    public void setEventSequence(int eventSequence) {
        this.eventSequence = eventSequence;
    }
}
