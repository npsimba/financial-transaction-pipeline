# Design Decisions — Financial Transaction Processing Pipeline

## 1. Technology Selection

The pipeline uses Java 8 and Maven because they provide a structured, maintainable environment for implementing data processing and validation.

Apache Commons CSV is used for CSV ingestion and output. JUnit 5 is used for automated testing. SLF4J and Logback provide application logging.

## 2. Financial Precision

Java `BigDecimal` is used for transaction amounts instead of floating-point types. This avoids precision errors during financial calculations and debit-credit reconciliation.

## 3. Transaction-Level Validation

Transactions are grouped using `transaction_id`. Validation is performed at both the individual-leg level and transaction-group level.

If a transaction group fails validation, all its legs are quarantined. This prevents partially valid transaction groups from entering curated output.

## 4. Duplicate Detection

Leg IDs and event IDs are checked for duplicates. Duplicate records are rejected to protect data integrity.

## 5. Event Ordering

Event sequence numbers and timestamps are validated to identify inconsistent event ordering. Deterministic ordering is used when processing transaction records.

## 6. Idempotency

The pipeline uses a SHA-256 fingerprint together with the transaction input file path to identify previously processed input files.

This prevents the same transaction input file from being processed repeatedly and creating duplicate output.

The current implementation does not independently fingerprint the account input file.

## 7. Reconciliation

The pipeline compares input, accepted, and rejected record counts. It also checks transaction debit-credit totals before publishing curated records.

## 8. Performance

A synthetic dataset of 100,000 transaction legs was processed successfully in approximately 2,021 milliseconds in a local test.

CSV-based processing and grouped validation provide a straightforward approach for this challenge's dataset size.

## 9. Known Limitations and Future Improvements

- Add row-level handling for malformed CSV records.
- Expand integration tests for empty input, unexpected schemas, and partial failures.
- Make output publishing atomic to improve recovery after failures.
- Include explicit start and end timestamps in the run report.
- Include account-file changes in idempotency tracking.
- Extend synthetic data labels to cover deliberately invalid scenarios.
