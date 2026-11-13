
package com.yottaflex.validation;

import com.yottaflex.model.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class EventOrderValidator {

    public HashMap<String, String> validateEventOrder(
            ArrayList<Transaction> transactions) {

        HashMap<String, ArrayList<Transaction>> groups =
                new HashMap<>();

        HashMap<String, String> errors = new HashMap<>();

        // Group transaction legs
        for (Transaction transaction : transactions) {

            String transactionId = transaction.getTransactionId();

            groups.putIfAbsent(
                    transactionId,
                    new ArrayList<Transaction>()
            );

            groups.get(transactionId).add(transaction);
        }

        // Validate every transaction group
        for (Map.Entry<String, ArrayList<Transaction>> entry
                : groups.entrySet()) {

            String transactionId = entry.getKey();
            ArrayList<Transaction> legs = entry.getValue();

            HashSet<Integer> sequences = new HashSet<>();

            // Check duplicate event sequences
            for (Transaction leg : legs) {

                if (!sequences.add(leg.getEventSequence())) {

                    errors.put(
                            transactionId,
                            "DUPLICATE_EVENT_SEQUENCE"
                    );

                    break;
                }
            }

            if (errors.containsKey(transactionId)) {
                continue;
            }

            // Sort by event sequence
            Collections.sort(legs, new Comparator<Transaction>() {

                public int compare(
                        Transaction first,
                        Transaction second) {

                    return Integer.compare(
                            first.getEventSequence(),
                            second.getEventSequence()
                    );
                }
            });

            // Check timestamp ordering
            for (int i = 1; i < legs.size(); i++) {

                if (legs.get(i).getEventTimestamp().isBefore(
                        legs.get(i - 1).getEventTimestamp())) {

                    errors.put(
                            transactionId,
                            "INVALID_EVENT_TIMESTAMP_ORDER"
                    );

                    break;
                }
            }
        }

        return errors;
    }
}
