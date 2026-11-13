
package com.yottaflex.validation;

import com.yottaflex.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DuplicateValidatorTest {

    private Transaction createTransaction(String legId, String eventId) {
        return new Transaction(
                legId,
                eventId,
                "TXN001",
                "ACC001",
                "DEBIT",
                new BigDecimal("500.00"),
                "INR",
                LocalDateTime.parse("2026-11-12T10:00:00"),
                1
        );
    }

    @Test
    public void testValidTransaction() {
        DuplicateValidator validator = new DuplicateValidator();

        assertNull(validator.validateDuplicate(
                createTransaction("LEG001", "EVT001")
        ));
    }

    @Test
    public void testDuplicateLegId() {
        DuplicateValidator validator = new DuplicateValidator();

        validator.validateDuplicate(
                createTransaction("LEG001", "EVT001")
        );

        assertEquals(
                "DUPLICATE_LEG_ID",
                validator.validateDuplicate(
                        createTransaction("LEG001", "EVT002")
                )
        );
    }

    @Test
    public void testDuplicateEventId() {
        DuplicateValidator validator = new DuplicateValidator();

        validator.validateDuplicate(
                createTransaction("LEG001", "EVT001")
        );

        assertEquals(
                "DUPLICATE_EVENT_ID",
                validator.validateDuplicate(
                        createTransaction("LEG002", "EVT001")
                )
        );
    }

    @Test
    public void testReset() {
        DuplicateValidator validator = new DuplicateValidator();

        validator.validateDuplicate(
                createTransaction("LEG001", "EVT001")
        );

        validator.reset();

        assertNull(validator.validateDuplicate(
                createTransaction("LEG001", "EVT001")
        ));
    }
}
