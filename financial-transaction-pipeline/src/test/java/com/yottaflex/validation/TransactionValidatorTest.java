
package com.yottaflex.validation;

import com.yottaflex.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionValidatorTest {

    private Transaction createTransaction(BigDecimal amount) {

        return new Transaction(
                "LEG001",
                "EVT001",
                "TXN001",
                "ACC001",
                "DEBIT",
                amount,
                "INR",
                LocalDateTime.parse("2026-11-12T10:00:00"),
                1
        );
    }

    @Test
    public void testValidTransaction() {

        TransactionValidator validator = new TransactionValidator();

        String result = validator.validateTransaction(
                createTransaction(new BigDecimal("500.00"))
        );

        assertNull(result);
    }

    @Test
    public void testNegativeAmount() {

        TransactionValidator validator = new TransactionValidator();

        String result = validator.validateTransaction(
                createTransaction(new BigDecimal("-100.00"))
        );

        assertEquals("INVALID_AMOUNT", result);
    }

    @Test
    public void testZeroAmount() {

        TransactionValidator validator = new TransactionValidator();

        String result = validator.validateTransaction(
                createTransaction(BigDecimal.ZERO)
        );

        assertEquals("INVALID_AMOUNT", result);
    }
}
