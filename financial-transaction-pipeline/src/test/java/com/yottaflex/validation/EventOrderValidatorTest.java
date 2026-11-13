
package com.yottaflex.validation;

import com.yottaflex.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class EventOrderValidatorTest {

    private Transaction createTransaction(
            String legId, int sequence, String timestamp) {

        return new Transaction(
                legId,
                "EVT" + legId,
                "TXN001",
                "ACC001",
                "DEBIT",
                new BigDecimal("100.00"),
                "INR",
                LocalDateTime.parse(timestamp),
                sequence
        );
    }

    @Test
    public void testValidEventOrder() {

        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(createTransaction(
                "LEG001", 1, "2026-11-12T10:00:00"));

        transactions.add(createTransaction(
                "LEG002", 2, "2026-11-12T10:01:00"));

        EventOrderValidator validator = new EventOrderValidator();

        HashMap<String, String> errors =
                validator.validateEventOrder(transactions);

        assertTrue(errors.isEmpty());
    }

    @Test
    public void testDuplicateEventSequence() {

        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(createTransaction(
                "LEG001", 1, "2026-11-12T10:00:00"));

        transactions.add(createTransaction(
                "LEG002", 1, "2026-11-12T10:01:00"));

        EventOrderValidator validator = new EventOrderValidator();

        HashMap<String, String> errors =
                validator.validateEventOrder(transactions);

        assertEquals(
                "DUPLICATE_EVENT_SEQUENCE",
                errors.get("TXN001")
        );
    }

    @Test
    public void testInvalidTimestampOrder() {

        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(createTransaction(
                "LEG001", 1, "2026-11-12T10:05:00"));

        transactions.add(createTransaction(
                "LEG002", 2, "2026-11-12T10:00:00"));

        EventOrderValidator validator = new EventOrderValidator();

        HashMap<String, String> errors =
                validator.validateEventOrder(transactions);

        assertEquals(
                "INVALID_EVENT_TIMESTAMP_ORDER",
                errors.get("TXN001")
        );
    }
}
