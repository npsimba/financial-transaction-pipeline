# financial-transaction-pipeline
A Java-based data engineering pipeline that ingests financial transaction records, validates debit and credit balances, detects duplicates and data-quality issues, and publishes clean records while quarantining invalid transactions.

 
# Financial Transaction Validation Pipeline

## 1. Project Overview

The Financial Transaction Validation Pipeline is a Java-based data engineering project designed to process and validate financial transaction records.

The pipeline checks transaction schemas, debit and credit balances, currencies, duplicate events, account references, and event ordering.

Valid transactions are published into curated output files, while invalid or anomalous records are stored separately in a quarantine area with rejection reasons.

## 2. Business Objective

The main objective is to ensure financial transaction data is accurate, consistent, reliable, and suitable for analytical use.

The pipeline prevents invalid and unbalanced transactions from entering curated outputs.

## 3. Technology Stack

- Java 8
- Maven
- CSV files
- JUnit 5
- SLF4J and Logback
- Git and GitHub

## 4. Architecture

The pipeline follows these stages:

1. Data Ingestion – Read transaction and account CSV files.
2. Schema Validation – Check required fields and data types.
3. Business Validation – Validate balances, currencies, account references, duplicates, and event ordering.
4. Transformation – Organize and prepare valid transaction records.
5. Publishing – Store valid records in curated output files.
6. Quarantine – Store rejected records with error reasons.
7. Monitoring – Generate execution logs and pipeline reports.

## 5. Project Structure

```text
financial-transaction-pipeline/
├── src/
│   ├── main/java/
│   └── test/java/
├── data/
│   ├── input/
│   ├── output/
│   ├── quarantine/
│   └── reports/
├── logs/
├── docs/
├── pom.xml
├── README.md
└── .gitignore
```

## 6. Data Sources

The project uses synthetic CSV data.

- transactions.csv – Financial transaction legs.
- accounts.csv – Account reference information.

Each transaction leg has a unique leg ID and event ID. Multiple legs are grouped using a transaction ID.

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
- Account references must exist.
- Duplicate event IDs and leg IDs are rejected.
- Event sequence numbers must be unique within a transaction.
- Late-arriving events are identified and processed in deterministic event order.
- Invalid transaction groups must not enter curated outputs.

## 9. Setup Instructions

### Prerequisites

- Java 8 or compatible JDK.
- Apache Maven 3.6 or later.
- Git.

### Clone the repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd financial-transaction-pipeline
```

### Build the project

```bash
mvn clean package
```

### Run automated tests

```bash
mvn test
```

### Run the application

```bash
mvn exec:java
```

## 10. Testing

Automated tests will cover:

- Valid transactions.
- Empty input.
- Malformed records.
- Missing fields.
- Duplicate records.
- Invalid account references.
- Debit and credit imbalances.
- Currency mismatches.
- Out-of-order events.
- Repeated pipeline execution.

## 11. Idempotency and Recovery

The pipeline uses stable transaction leg identifiers to prevent duplicate records during reruns.

Run information, processing counts, and rejected records are maintained for traceability.

Failed runs can be investigated and retried without unintentionally duplicating previously processed records.

## 12. Performance

A configurable synthetic data generator will support large-scale testing, including 100,000 or more records.

Execution time and processing statistics will be measured.

## 13. Limitations and Future Improvements

- Current implementation uses local CSV files.
- A production implementation could use a relational database.
- Large-scale processing could be extended using Apache Spark.
- Cloud storage and automated scheduling could be added in the future.
