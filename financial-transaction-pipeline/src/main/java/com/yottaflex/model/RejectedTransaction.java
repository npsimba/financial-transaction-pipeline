
package com.yottaflex.model;

public class RejectedTransaction {

    private Transaction transaction;
    private String rejectionReason;

    public RejectedTransaction(Transaction transaction, String rejectionReason) {
        this.transaction = transaction;
        this.rejectionReason = rejectionReason;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }
}
