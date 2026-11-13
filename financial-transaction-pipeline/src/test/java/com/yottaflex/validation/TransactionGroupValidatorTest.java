
package com.yottaflex.validation;

import com.yottaflex.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionGroupValidatorTest {

    private Transaction createTransaction(
            String legId, String currency) {

        return new Transaction(
                legId, "EVT" + legId, "TXN001",
                "ACC001", "DEBIT",
                new BigDecimal("100.00"), currency,
                LocalDateTime.parse("2026-11-12T10:00:00"),
                1
        );
    }

    @Test
    public void testSameCurrency() {

        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(createTransaction("LEG001", "INR"));
        transactions.add(createTransaction("LEG002", "INR"));

        TransactionGroupValidator validator =
                new TransactionGroupValidator();

        assertTrue(
                validator.validateCurrencyConsistency(transactions).isEmpty()
        );
    }

    @Test
    public void testMixedCurrency() {

        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(createTransaction("LEG001", "INR"));
        transactions.add(createTransaction("LEG002", "USD"));

        TransactionGroupValidator validator =
                new TransactionGroupValidator();

        assertEquals(
                "MIXED_CURRENCY_IN_TRANSACTION",
                validator.validateCurrencyConsistency(transactions)
                        .get("TXN001")
        );
    }
}
