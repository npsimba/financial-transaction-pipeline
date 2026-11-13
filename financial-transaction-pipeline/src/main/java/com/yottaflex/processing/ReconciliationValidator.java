
package com.yottaflex.processing;

import com.yottaflex.model.Transaction;
import com.yottaflex.model.RejectedTransaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReconciliationValidator {

    public ArrayList<String> validate(
            ArrayList<Transaction> input,
            ArrayList<Transaction> accepted,
            ArrayList<RejectedTransaction> rejected) {

        ArrayList<String> errors = new ArrayList<>();

        // Check input count reconciliation
        int totalProcessed = accepted.size() + rejected.size();

        if (input.size() != totalProcessed) {
            errors.add("ROW_COUNT_MISMATCH");
        }

        // Calculate debit and credit totals for accepted transactions
        HashMap<String, BigDecimal> debits = new HashMap<>();
        HashMap<String, BigDecimal> credits = new HashMap<>();

        for (Transaction transaction : accepted) {

            String key = transaction.getTransactionId()
                    + "_" + transaction.getCurrency();

            if ("DEBIT".equalsIgnoreCase(transaction.getLegType())) {

                debits.put(
                        key,
                        debits.getOrDefault(key, BigDecimal.ZERO)
                                .add(transaction.getAmount())
                );

            } else if ("CREDIT".equalsIgnoreCase(
                    transaction.getLegType())) {

                credits.put(
                        key,
                        credits.getOrDefault(key, BigDecimal.ZERO)
                                .add(transaction.getAmount())
                );
            }
        }

        // Verify every accepted transaction is balanced
        for (Map.Entry<String, BigDecimal> entry : debits.entrySet()) {

            BigDecimal credit = credits.getOrDefault(
                    entry.getKey(),
                    BigDecimal.ZERO
            );

            if (entry.getValue().compareTo(credit) != 0) {
                errors.add("UNBALANCED_ACCEPTED_TRANSACTION: "
                        + entry.getKey());
            }
        }

        for (String key : credits.keySet()) {

            if (!debits.containsKey(key)) {
                errors.add("CREDIT_WITHOUT_DEBIT: " + key);
            }
        }

        return errors;
    }
}
