
# Financial Transaction Validation Pipeline

A Java-based data engineering pipeline that ingests financial transaction records, validates debit and credit balances, detects duplicates and data-quality issues, and publishes clean records while quarantining invalid transactions.

## 1. Project Overview

The Financial Transaction Validation Pipeline is a Java-based data engineering project designed to process and validate financial transaction records.

The pipeline checks transaction schemas, debit and credit balances, currencies, duplicate events, account references, and event ordering.

Valid transactions are published into curated output files, while invalid transaction groups are stored separately in a quarantine area with rejection reasons.

## 2. Business Objective

The main objective is to ensure financial transaction data is accurate, consistent, reliable, and suitable for analytical use.

The pipeline prevents invalid and unbalanced transactions from entering curated outputs.

## 3. Technology Stack

- Java 8
- Apache Maven
- Apache Commons CSV
- JUnit 5
- SLF4J and Logback
- CSV files
- Git and GitHub

## 4. Architecture

The pipeline follows these stages:

1. Data Ingestion – Read transaction and account CSV files.
2. Schema Validation – Check required fields and data types.
3. Business Validation – Validate balances, currencies, account references, duplicates, and event ordering.
4. Transformation – Organize and prepare valid transaction records.
5. Reconciliation – Verify accepted and rejected record counts and financial balances.
6. Publishing – Store valid records in curated output files.
7. Quarantine – Store rejected transaction records with rejection reasons.
8. Monitoring – Generate execution logs and pipeline reports.

## 5. Project Structure

```text
financial-transaction-pipeline/
├── README.md
├── data_dictionary.md
├── docs/
│   └── architecture.md
└── financial-transaction-pipeline/
    ├── pom.xml
    ├── .gitignore
    ├── data/
    │   ├── input/
    │   ├── expected/
    │   ├── output/
    │   ├── quarantine/
    │   └── reports/
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   └── resources/
    │   └── test/
    │       └── java/
    └── logs/
```

## 6. Data Sources

The project uses synthetic CSV data.

- `transactions.csv` – Financial transaction leg records.
- `accounts.csv` – Account reference information.

Each transaction leg has a unique leg ID and event ID. Multiple legs are grouped using a transaction ID.

A synthetic data generator creates 100,000 transaction legs across 50,000 transaction groups using a fixed random seed.

## 7. Output

The pipeline produces:

- Curated transaction records.
- Rejected transaction records with validation reasons.
- Pipeline execution reports.
- Application logs.

## 8. Business Rules

- Debit and credit totals must balance for each transaction and currency.
- Transaction amounts must be positive.
- Required fields cannot be null or empty.
- Currency must be valid and consistent within a transaction.
- Referenced accounts must exist and be active.
- Duplicate event IDs and leg IDs are rejected.
- Event sequence numbers must be unique within a transaction.
- Event timestamps must follow the defined event sequence.
- Invalid transaction groups must not enter curated outputs.

## 9. Setup Instructions

### Prerequisites

- Java 8 or compatible JDK.
- Apache Maven 3.6 or later.
- Git.

### Clone the repository

```bash
git clone https://github.com/npsimba/financial-transaction-pipeline.git
cd financial-transaction-pipeline
```

### Navigate to the Maven project

```bash
cd financial-transaction-pipeline
```

### Build the project

```bash
mvn clean package
```

### Run automated tests

```bash
mvn clean test
```

### Run the sample pipeline

```bash
mvn exec:java
```

### Generate synthetic data

```bash
java -cp target/classes com.yottaflex.utils.SyntheticDataGenerator
```

### Process the generated dataset

```bash
mvn exec:java "-Dexec.args=data/input/generated_transactions.csv data/input/generated_accounts.csv"
```

## 10. Testing and Validation

The project includes automated JUnit tests for:

- Transaction field validation.
- Account validation.
- Duplicate leg and event detection.
- Event ordering validation.
- Currency consistency.
- Debit and credit balance validation.

The current test suite contains 18 automated tests, all passing.

## 11. Idempotency and Recovery

The pipeline uses SHA-256 file fingerprints and input file paths to identify previously processed transaction files.

Repeated processing of an already processed input file is skipped to prevent unintended duplicate processing.

Run IDs, processing counts, rejected records, and execution reports support traceability.

## 12. Performance Testing

A synthetic dataset containing 100,000 transaction legs across 50,000 transaction groups was generated using a fixed random seed.

Verified execution results:

| Metric | Result |
|---|---:|
| Input records | 100,000 |
| Accepted records | 100,000 |
| Rejected records | 0 |
| Errors | 0 |
| Processing time | 2,021 ms |
| Pipeline status | SUCCESS |

## 13. Limitations and Future Improvements

- Current implementation uses local CSV files.
- A production implementation could use a relational database.
- Large-scale processing could be extended using Apache Spark.
- Cloud storage and automated scheduling could be added in the future.
- Additional monitoring and recovery mechanisms could be introduced.
