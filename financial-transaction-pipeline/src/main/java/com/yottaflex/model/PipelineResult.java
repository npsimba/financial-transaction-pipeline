
package com.yottaflex.model;

import java.util.ArrayList;

public class PipelineResult {

    private ArrayList<Transaction> acceptedTransactions;
    private ArrayList<RejectedTransaction> rejectedTransactions;
    private String status;

    public PipelineResult() {
        acceptedTransactions = new ArrayList<>();
        rejectedTransactions = new ArrayList<>();
        status = "STARTED";
    }

    public ArrayList<Transaction> getAcceptedTransactions() {
        return acceptedTransactions;
    }

    public ArrayList<RejectedTransaction> getRejectedTransactions() {
        return rejectedTransactions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
