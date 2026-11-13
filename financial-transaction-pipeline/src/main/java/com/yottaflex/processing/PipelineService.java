
package com.yottaflex.processing;

import com.yottaflex.ingestion.AccountReader;
import com.yottaflex.ingestion.TransactionReader;
import com.yottaflex.model.*;
import com.yottaflex.reporting.*;
import com.yottaflex.utils.LoggerUtil;
import com.yottaflex.utils.RunIdGenerator;
import com.yottaflex.validation.*;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

public class PipelineService {

    private static final Logger logger =
            LoggerUtil.getLogger(PipelineService.class);

    public PipelineResult execute(
            String transactionFile,
            String accountFile) throws Exception {

        long startTime = System.nanoTime();

        PipelineResult result = new PipelineResult();
        String runId = RunIdGenerator.generateRunId();

        IdempotencyManager idempotency = new IdempotencyManager();

        if (idempotency.hasAlreadyProcessed(transactionFile)) {
            result.setStatus("SKIPPED_ALREADY_PROCESSED");
            logger.info("Input file already processed.");
            return result;
        }

        logger.info("Pipeline started. Run ID: {}", runId);

        AccountReader accountReader = new AccountReader();
        TransactionReader transactionReader = new TransactionReader();

        ArrayList<Account> accounts =
                accountReader.readAccounts(accountFile);

        ArrayList<Transaction> transactions =
                transactionReader.readTransactions(transactionFile);

        AccountValidator accountValidator = new AccountValidator();
        accountValidator.loadAccounts(accounts);

        TransactionValidator transactionValidator =
                new TransactionValidator();

        DuplicateValidator duplicateValidator =
                new DuplicateValidator();

        HashMap<String, String> groupErrors = new HashMap<>();

        HashMap<String, ArrayList<Transaction>> groups =
                new HashMap<>();

        // Group records by transaction ID
        for (Transaction transaction : transactions) {

            String transactionId = transaction.getTransactionId();

            if (transactionId != null &&
                    !transactionId.trim().isEmpty()) {

                groups.putIfAbsent(
                        transactionId,
                        new ArrayList<Transaction>()
                );

                groups.get(transactionId).add(transaction);
            }
        }

        // Validate individual records
        for (Transaction transaction : transactions) {

            String reason =
                    transactionValidator.validateTransaction(transaction);

            if (reason == null) {
                reason = accountValidator.validateAccount(
                        transaction.getAccountId()
                );
            }

            if (reason == null) {
                reason = duplicateValidator.validateDuplicate(transaction);
            }

            if (reason != null) {

                String transactionId = transaction.getTransactionId();

                if (transactionId != null &&
                        !transactionId.trim().isEmpty()) {

                    groupErrors.putIfAbsent(transactionId, reason);

                } else {

                    result.getRejectedTransactions().add(
                            new RejectedTransaction(transaction, reason)
                    );
                }
            }
        }

        TransactionGroupValidator groupValidator =
                new TransactionGroupValidator();

        TransactionProcessor processor =
                new TransactionProcessor();

        EventOrderValidator orderValidator =
                new EventOrderValidator();

        // Keep only groups that passed individual validation
        ArrayList<Transaction> validTransactions =
                new ArrayList<>();

        for (Map.Entry<String, ArrayList<Transaction>> entry
                : groups.entrySet()) {

            if (!groupErrors.containsKey(entry.getKey())) {
                validTransactions.addAll(entry.getValue());
            }
        }

        // Validate currency consistency
        HashMap<String, String> currencyErrors =
                groupValidator.validateCurrencyConsistency(
                        validTransactions
                );

        for (Map.Entry<String, String> error
                : currencyErrors.entrySet()) {

            groupErrors.putIfAbsent(
                    error.getKey(),
                    error.getValue()
            );
        }

        // Validate event ordering
        HashMap<String, String> orderErrors =
                orderValidator.validateEventOrder(validTransactions);

        for (Map.Entry<String, String> error
                : orderErrors.entrySet()) {

            groupErrors.putIfAbsent(
                    error.getKey(),
                    error.getValue()
            );

            logger.warn(
                    "Event ordering issue in transaction {}: {}",
                    error.getKey(),
                    error.getValue()
            );
        }

        // Prepare transactions for balance validation
        ArrayList<Transaction> balanceCheckTransactions =
                new ArrayList<>();

        for (Map.Entry<String, ArrayList<Transaction>> entry
                : groups.entrySet()) {

            if (!groupErrors.containsKey(entry.getKey())) {
                balanceCheckTransactions.addAll(entry.getValue());
            }
        }

        // Check debit and credit balance
        ArrayList<Transaction> unbalanced =
                processor.findUnbalancedTransactions(
                        balanceCheckTransactions
                );

        for (Transaction transaction : unbalanced) {

            groupErrors.putIfAbsent(
                    transaction.getTransactionId(),
                    "UNBALANCED_TRANSACTION"
            );
        }

        // Separate accepted and rejected records
        for (Map.Entry<String, ArrayList<Transaction>> entry
                : groups.entrySet()) {

            String transactionId = entry.getKey();

            if (groupErrors.containsKey(transactionId)) {

                String reason = groupErrors.get(transactionId);

                for (Transaction transaction : entry.getValue()) {
                    result.getRejectedTransactions().add(
                            new RejectedTransaction(transaction, reason)
                    );
                }

            } else {
                result.getAcceptedTransactions().addAll(entry.getValue());
            }
        }

        // Sort output for deterministic results
        Collections.sort(
                result.getAcceptedTransactions(),
                Comparator.comparing(Transaction::getLegId)
        );

        Collections.sort(
                result.getRejectedTransactions(),
                Comparator.comparing(
                        rejected -> rejected.getTransaction().getLegId(),
                        Comparator.nullsFirst(Comparator.naturalOrder())
                )
        );

        // Reconciliation checks
        ReconciliationValidator reconciliationValidator =
                new ReconciliationValidator();

        ArrayList<String> reconciliationErrors =
                reconciliationValidator.validate(
                        transactions,
                        result.getAcceptedTransactions(),
                        result.getRejectedTransactions()
                );

        for (String error : reconciliationErrors) {
            logger.error("Reconciliation error: {}", error);
        }

        if (reconciliationErrors.isEmpty()) {
            result.setStatus("SUCCESS");
        } else {
            result.setStatus("FAILED_RECONCILIATION");
        }

        // Prepare execution report
        RunReport report = new RunReport(runId);

        report.setInputCount(transactions.size());

        report.setAcceptedCount(
                result.getAcceptedTransactions().size()
        );

        report.setRejectedCount(
                result.getRejectedTransactions().size()
        );

        report.setErrorCount(reconciliationErrors.size());
        report.setStatus(result.getStatus());

        // Output paths
        String outputPath =
                "data/output/curated_" + runId + ".csv";

        String quarantinePath =
                "data/quarantine/rejected_" + runId + ".csv";

        String reportPath =
                "data/reports/report_" + runId + ".csv";

        // Publish accepted records
        new TransactionWriter().writeTransactions(
                result.getAcceptedTransactions(),
                outputPath,
                runId,
                transactionFile
        );

        // Publish rejected records
        new QuarantineWriter().writeRejectedTransactions(
                result.getRejectedTransactions(),
                quarantinePath,
                runId,
                transactionFile
        );

        // Publish execution report
        new ReportWriter().writeReport(report, reportPath);

        // Mark processed only when reconciliation succeeds
        if (reconciliationErrors.isEmpty()) {
            idempotency.markAsProcessed(transactionFile);
        }

        long elapsedMillis =
                (System.nanoTime() - startTime) / 1_000_000;

        logger.info(
                "Pipeline completed. Accepted: {}, Rejected: {}, Errors: {}, Time: {} ms",
                report.getAcceptedCount(),
                report.getRejectedCount(),
                report.getErrorCount(),
                elapsedMillis
        );

        return result;
    }
}
