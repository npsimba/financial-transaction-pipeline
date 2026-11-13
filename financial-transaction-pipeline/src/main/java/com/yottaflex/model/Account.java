
package com.yottaflex.model;

public class Account {

    private String accountId;
    private String accountStatus;

    // Default constructor
    public Account() {
    }

    // Parameterized constructor
    public Account(String accountId, String accountStatus) {
        this.accountId = accountId;
        this.accountStatus = accountStatus;
    }

    // Getter and Setter for accountId
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    // Getter and Setter for accountStatus
    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
