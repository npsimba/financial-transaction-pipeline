package com.yottaflex.processing;

import com.yottaflex.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionProcessorTest {

    private Transaction createTransaction(String legId, String type, String amount) {
        return new Transaction(
                legId,
                "EVT" + legId,
                "TXN001",
                "ACC001",
                type,
                new BigDecimal(amount),
                "INR",
                LocalDateTime.parse("2026-11-12T10:00:00"),
                legId.equals("LEG001") ? 1 : 2
        );
    }

    @Test
    public void testBalancedTransaction() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(createTransaction("LEG001", "DEBIT", "500"));
        transactions.add(createTransaction("LEG002", "CREDIT", "500"));

        TransactionProcessor processor = new TransactionProcessor();

        ArrayList<Transaction> result =
                processor.findUnbalancedTransactions(transactions);

        assertEquals(0, result.size());
    }

    @Test
    public void testUnbalancedTransaction() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(createTransaction("LEG001", "DEBIT", "500"));
        transactions.add(createTransaction("LEG002", "CREDIT", "300"));

        TransactionProcessor processor = new TransactionProcessor();

        ArrayList<Transaction> result =
                processor.findUnbalancedTransactions(transactions);

        assertEquals(2, result.size());
    }
}