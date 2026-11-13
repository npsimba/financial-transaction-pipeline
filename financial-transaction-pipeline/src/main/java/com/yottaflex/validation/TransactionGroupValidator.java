
package com.yottaflex.validation;

import com.yottaflex.model.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TransactionGroupValidator {

    public HashMap<String, String> validateCurrencyConsistency(
            ArrayList<Transaction> transactions) {

        HashMap<String, HashSet<String>> currencies = new HashMap<>();
        HashMap<String, String> errors = new HashMap<>();

        for (Transaction transaction : transactions) {

            String transactionId = transaction.getTransactionId();

            currencies.putIfAbsent(transactionId, new HashSet<String>());

            currencies.get(transactionId).add(
                    transaction.getCurrency().toUpperCase()
            );
        }

        for (String transactionId : currencies.keySet()) {

            if (currencies.get(transactionId).size() > 1) {

                errors.put(
                        transactionId,
                        "MIXED_CURRENCY_IN_TRANSACTION"
                );
            }
        }

        return errors;
    }
}
