# Financial Transaction Processing Pipeline

A Java-based data engineering pipeline that ingests financial transaction records, validates transaction legs, checks account references and debit-credit balances, quarantines invalid records, and publishes curated output with reconciliation reports.

## Features

- CSV ingestion for transactions and accounts.
- Schema and required-field validation.
- Account status and reference validation.
- Duplicate transaction-leg and event detection.
- Currency consistency and event-order validation.
- Debit-credit balance reconciliation using `BigDecimal`.
- Invalid transaction groups are quarantined.
- Deterministic output ordering.
- SHA-256 input fingerprint-based protection against processing the same transaction file twice.
- Run reports, logging, and reconciliation checks.
- Synthetic data generator with a fixed seed.

## Technology

- Java 8
- Maven
- Apache Commons CSV
- JUnit 5
- SLF4J and Logback

## Repository Structure

```text
financial-transaction-pipeline/
├── README.md
├── data_dictionary.md
├── docs/
│   └── architecture.md
└── financial-transaction-pipeline/
    ├── pom.xml
    ├── data/
    │   ├── input/
    │   ├── expected/
    │   ├── output/
    │   ├── quarantine/
    │   └── reports/
    ├── src/
    │   ├── main/
    │   └── test/
    └── logs/
```

## Requirements

- Java 8 or later
- Maven 3.9 or later

## Setup

Clone the repository:

```bash
git clone https://github.com/npsimba/financial-transaction-pipeline.git
cd financial-transaction-pipeline
cd financial-transaction-pipeline
```

Run automated tests:

```bash
mvn clean test
```

Run the sample pipeline:

```bash
mvn exec:java "-Dexec.args=data/input/transactions.csv data/input/accounts.csv"
```

The pipeline writes accepted transactions, rejected records, and a run report into the corresponding output directories.

## Generate Synthetic Data

Run:

```bash
java -cp target/classes com.yottaflex.utils.SyntheticDataGenerator
```

The generator uses a fixed random seed (`42`) and produces:

- 100,000 transaction legs
- 50,000 transaction groups
- 100 accounts
- Expected labels in a separate expected-data file

Run the generated dataset:

```bash
mvn exec:java "-Dexec.args=data/input/generated_transactions.csv data/input/generated_accounts.csv"
```

## Validation Rules

The pipeline checks:

1. Required transaction fields and valid values.
2. Unique leg IDs and event IDs.
3. Valid account references and active account status.
4. Positive transaction amounts.
5. Consistent currency within a transaction.
6. Valid event sequence and timestamp ordering.
7. Equal debit and credit totals for each transaction.
8. Reconciliation between input, accepted, and rejected records.

A transaction group that fails validation is quarantined rather than published as curated data.

## Test Results

The current automated test suite contains 18 tests covering transaction validation, account validation, duplicate detection, event ordering, and transaction-group processing.

Run the tests with:

```bash
mvn clean test
```

## Performance Test

A synthetic dataset containing 100,000 transaction legs was processed successfully.

| Metric | Result |
|---|---:|
| Input records | 100,000 |
| Accepted records | 100,000 |
| Rejected records | 0 |
| Errors | 0 |
| Execution time | 2,021 ms |
| Status | SUCCESS |

Results are from a local execution and may vary by machine.

## Idempotency and Recovery

The pipeline uses a SHA-256 fingerprint and input file path to identify previously processed transaction files. Re-running the same transaction input is skipped to avoid duplicate processing.

## Documentation

- [Data Dictionary](data_dictionary.md)
- [Architecture](docs/architecture.md)

## Limitations and Future Improvements

- Add row-level quarantine handling for malformed CSV records.
- Extend integration tests for empty batches, schema changes, and partial failures.
- Improve atomic publishing and recovery after output-write failures.
- Include explicit run start and end timestamps in the run report.
- Extend idempotency tracking to include account input changes.

## Author

Developed as a financial transaction data pipeline engineering challenge.
