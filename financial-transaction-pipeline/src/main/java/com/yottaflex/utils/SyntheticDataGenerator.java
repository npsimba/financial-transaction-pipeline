
package com.yottaflex.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

public class SyntheticDataGenerator {

    public static void main(String[] args) throws IOException {

        int transactionCount = 50000;
        long seed = 42L;

        Random random = new Random(seed);

        new File("data/input").mkdirs();
        new File("data/expected").mkdirs();

        BufferedWriter transactions = new BufferedWriter(
                new FileWriter("data/input/generated_transactions.csv"));

        BufferedWriter accounts = new BufferedWriter(
                new FileWriter("data/input/generated_accounts.csv"));

        BufferedWriter expected = new BufferedWriter(
                new FileWriter("data/expected/generated_expected.csv"));

        transactions.write(
                "leg_id,event_id,transaction_id,account_id,leg_type,amount,currency,event_timestamp,event_sequence\n");

        accounts.write("account_id,account_status\n");

        expected.write("transaction_id,expected_status\n");

        // Generate 100 accounts
        for (int i = 1; i <= 100; i++) {
            accounts.write(String.format("ACC%03d,ACTIVE%n", i));
        }

        LocalDateTime baseTime =
                LocalDateTime.parse("2026-11-12T10:00:00");

        // Generate 50,000 balanced transactions
        for (int i = 1; i <= transactionCount; i++) {

            String transactionId = String.format("TXN%06d", i);

            String debitLeg = String.format("LEG%06d", (i * 2) - 1);
            String creditLeg = String.format("LEG%06d", i * 2);

            String debitEvent = String.format("EVT%06d", (i * 2) - 1);
            String creditEvent = String.format("EVT%06d", i * 2);

            String debitAccount =
                    String.format("ACC%03d", random.nextInt(100) + 1);

            String creditAccount =
                    String.format("ACC%03d", random.nextInt(100) + 1);

            BigDecimal amount = new BigDecimal(
                    100 + random.nextInt(9901)
            ).setScale(2);

            LocalDateTime timestamp =
                    baseTime.plusSeconds(i * 10L);

            transactions.write(
                    debitLeg + "," + debitEvent + "," +
                            transactionId + "," + debitAccount +
                            ",DEBIT," + amount + ",INR," +
                            timestamp + ",1\n"
            );

            transactions.write(
                    creditLeg + "," + creditEvent + "," +
                            transactionId + "," + creditAccount +
                            ",CREDIT," + amount + ",INR," +
                            timestamp.plusSeconds(5) + ",2\n"
            );

            expected.write(
                    transactionId + ",BALANCED\n"
            );
        }

        transactions.close();
        accounts.close();
        expected.close();

        System.out.println("Synthetic data generation completed.");
        System.out.println("Transactions: 100000 legs");
        System.out.println("Transaction groups: 50000");
        System.out.println("Accounts: 100");
        System.out.println("Random seed: " + seed);
    }
}
