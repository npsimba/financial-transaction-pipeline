
package com.yottaflex.processing;

import com.yottaflex.model.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class TransactionProcessor {

    public ArrayList<Transaction> findUnbalancedTransactions(
            ArrayList<Transaction> transactions) {

        HashMap<String, BigDecimal> debitTotals = new HashMap<>();
        HashMap<String, BigDecimal> creditTotals = new HashMap<>();

        for (Transaction transaction : transactions) {

            String key = transaction.getTransactionId()
                    + "_" + transaction.getCurrency();

            if (transaction.getLegType().equalsIgnoreCase("DEBIT")) {

                BigDecimal total = debitTotals.getOrDefault(
                        key, BigDecimal.ZERO);

                debitTotals.put(key, total.add(transaction.getAmount()));

            } else if (transaction.getLegType().equalsIgnoreCase("CREDIT")) {

                BigDecimal total = creditTotals.getOrDefault(
                        key, BigDecimal.ZERO);

                creditTotals.put(key, total.add(transaction.getAmount()));
            }
        }

        ArrayList<Transaction> unbalanced = new ArrayList<>();

        for (Transaction transaction : transactions) {

            String key = transaction.getTransactionId()
                    + "_" + transaction.getCurrency();

            BigDecimal debit = debitTotals.getOrDefault(
                    key, BigDecimal.ZERO);

            BigDecimal credit = creditTotals.getOrDefault(
                    key, BigDecimal.ZERO);

            if (debit.compareTo(credit) != 0) {
                unbalanced.add(transaction);
            }
        }

        return unbalanced;
    }
}
