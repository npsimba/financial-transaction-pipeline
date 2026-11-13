
package com.yottaflex.validation;

import com.yottaflex.model.Transaction;

import java.math.BigDecimal;

public class TransactionValidator {

    public String validateTransaction(Transaction transaction) {

        if (transaction == null) {
            return "NULL_TRANSACTION";
        }

        if (isEmpty(transaction.getLegId())) {
            return "MISSING_LEG_ID";
        }

        if (isEmpty(transaction.getEventId())) {
            return "MISSING_EVENT_ID";
        }

        if (isEmpty(transaction.getTransactionId())) {
            return "MISSING_TRANSACTION_ID";
        }

        if (isEmpty(transaction.getAccountId())) {
            return "MISSING_ACCOUNT_ID";
        }

        if (transaction.getAmount() == null) {
            return "MISSING_AMOUNT";
        }

        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return "INVALID_AMOUNT";
        }

        if (transaction.getLegType() == null ||
                (!transaction.getLegType().equalsIgnoreCase("DEBIT") &&
                        !transaction.getLegType().equalsIgnoreCase("CREDIT"))) {
            return "INVALID_LEG_TYPE";
        }

        if (isEmpty(transaction.getCurrency()) ||
                transaction.getCurrency().trim().length() != 3) {
            return "INVALID_CURRENCY";
        }

        if (transaction.getEventTimestamp() == null) {
            return "MISSING_EVENT_TIMESTAMP";
        }

        if (transaction.getEventSequence() <= 0) {
            return "INVALID_EVENT_SEQUENCE";
        }

        return null;
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
