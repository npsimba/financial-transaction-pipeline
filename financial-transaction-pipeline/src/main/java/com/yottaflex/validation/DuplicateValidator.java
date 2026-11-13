
package com.yottaflex.validation;

import com.yottaflex.model.Transaction;

import java.util.ArrayList;
import java.util.HashSet;

public class DuplicateValidator {

    private HashSet<String> legIds = new HashSet<>();
    private HashSet<String> eventIds = new HashSet<>();

    public void reset() {
        legIds.clear();
        eventIds.clear();
    }

    public String validateDuplicate(Transaction transaction) {

        if (transaction == null) {
            return "NULL_TRANSACTION";
        }

        if (legIds.contains(transaction.getLegId())) {
            return "DUPLICATE_LEG_ID";
        }

        if (eventIds.contains(transaction.getEventId())) {
            return "DUPLICATE_EVENT_ID";
        }

        legIds.add(transaction.getLegId());
        eventIds.add(transaction.getEventId());

        return null;
    }
}
