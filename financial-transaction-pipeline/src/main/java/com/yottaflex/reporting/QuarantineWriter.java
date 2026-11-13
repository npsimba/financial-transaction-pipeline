
package com.yottaflex.reporting;

import com.yottaflex.model.RejectedTransaction;
import com.yottaflex.model.Transaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class QuarantineWriter {

    public void writeRejectedTransactions(
            ArrayList<RejectedTransaction> rejectedTransactions,
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
                        "rejection_reason",
                        "run_id",
                        "source_file",
                        "processed_at"
                )
        );

        String processedAt = LocalDateTime.now().toString();

        for (RejectedTransaction rejected : rejectedTransactions) {

            Transaction transaction = rejected.getTransaction();

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
                    rejected.getRejectionReason(),
                    runId,
                    sourceFile,
                    processedAt
            );
        }

        printer.close();
    }
}
