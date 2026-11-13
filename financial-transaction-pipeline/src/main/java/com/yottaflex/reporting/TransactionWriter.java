
package com.yottaflex.reporting;

import com.yottaflex.model.Transaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TransactionWriter {

    public void writeTransactions(
            ArrayList<Transaction> transactions,
            String filePath,
            String runId,
            String sourceFile) throws Exception {

        Files.createDirectories(Paths.get(filePath).getParent());

        Writer writer = new FileWriter(filePath);

        CSVPrinter printer = new CSVPrinter(
                writer,
                CSVFormat.DEFAULT.withHeader(
                        "leg_id",
                        "event_id",
                        "transaction_id",
                        "account_id",
                        "leg_type",
                        "amount",
                        "currency",
                        "event_timestamp",
                        "event_sequence",
                        "run_id",
                        "source_file",
                        "processed_at"
                )
        );

        String processedAt = LocalDateTime.now().toString();

        for (Transaction transaction : transactions) {

            printer.printRecord(
                    transaction.getLegId(),
                    transaction.getEventId(),
                    transaction.getTransactionId(),
                    transaction.getAccountId(),
                    transaction.getLegType(),
                    transaction.getAmount(),
                    transaction.getCurrency(),
                    transaction.getEventTimestamp(),
                    transaction.getEventSequence(),
                    runId,
                    sourceFile,
                    processedAt
            );
        }

        printer.close();
    }
}
