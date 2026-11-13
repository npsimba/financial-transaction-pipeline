# Data Dictionary — Financial Transaction Processing Pipeline

## 1. Overview

This document describes the source data, target data, entities, fields, relationships, and validation rules used by the Financial Transaction Processing Pipeline.

The pipeline processes financial transaction legs, validates their integrity, checks debit-credit balances, and separates accepted and rejected records.

## 2. Source Data

### 2.1 Transactions — `transactions.csv`

**Grain:** One row represents one transaction leg.

| Column | Data Type | Description |
|---|---|---|
| leg_id | String | Unique identifier for a transaction leg |
| event_id | String | Unique event identifier |
| transaction_id | String | Identifier linking related transaction legs |
| account_id | String | Account associated with the transaction leg |
| leg_type | String | Debit or Credit |
| amount | Decimal | Transaction amount |
| currency | String | Currency code |
| event_timestamp | DateTime | Timestamp of the transaction event |
| event_sequence | Integer | Sequence number of the event |

### 2.2 Accounts — `accounts.csv`

**Grain:** One row represents one account.

| Column | Data Type | Description |
|---|---|---|
| account_id | String | Unique account identifier |
| account_status | String | Account status: ACTIVE or INACTIVE |

## 3. Target Data

### 3.1 Curated Transactions

Contains transaction legs that pass the pipeline's validation and reconciliation rules.

The output preserves the transaction fields and includes processing metadata:

| Field | Description |
|---|---|
| run_id | Identifier of the processing run |
| source_file | Source transaction file |
| processed_at | Processing timestamp |

### 3.2 Quarantined Transactions

Contains transaction legs rejected during validation.

| Field | Description |
|---|---|
| rejection_reason | Explanation for rejection |
| run_id | Identifier of the processing run |
| source_file | Source transaction file |
| processed_at | Processing timestamp |

Rejected transaction groups are kept out of the curated output.

### 3.3 Pipeline Run Report

The report records the outcome of a pipeline execution.

| Column | Data Type | Description |
|---|---|---|
| run_id | String | Unique pipeline run identifier |
| input_count | Integer | Number of input records |
| accepted_count | Integer | Number of accepted records |
| rejected_count | Integer | Number of rejected records |
| error_count | Integer | Number of processing errors |
| status | String | Pipeline execution status |

Execution start, completion, and elapsed-time information are also recorded through application logging.

## 4. Entity Relationships

- One transaction can contain multiple transaction legs.
- Each transaction leg references an account.
- Each account can be associated with multiple transaction legs.
- Each pipeline run processes a source transaction file and produces accepted records, rejected records, and a run report.

## 5. Primary Keys and Relationships

| Entity | Key | Purpose |
|---|---|---|
| Transaction leg | leg_id | Uniquely identifies a transaction leg |
| Event | event_id | Identifies a transaction event |
| Transaction | transaction_id | Groups related transaction legs |
| Account | account_id | Identifies an account |
| Pipeline run | run_id | Identifies a processing execution |

## 6. Data Quality and Business Rules

1. Required fields must be present.
2. Transaction leg IDs and event IDs must be unique.
3. Transaction amounts must be positive.
4. Every transaction leg must reference a valid active account.
5. Currency must be consistent within a transaction.
6. Event sequence and timestamp ordering must be validated.
7. Debit and credit totals must balance for each transaction.
8. Invalid transaction groups must be quarantined.
9. Accepted and rejected record counts must reconcile with the input count.

## 7. Data Types and Precision

- Financial amounts use Java `BigDecimal` to avoid floating-point precision issues.
- Identifiers are represented as strings.
- Event timestamps use date-time values.
- Event sequence and record counts use integer values.
- Currency is represented using a currency code.

## 8. Assumptions and Limitations

- Input data is supplied as CSV files.
- The account input contains account identifiers and account status.
- A transaction consists of one or more related transaction legs.
- A balanced transaction has equal debit and credit totals.
- The current idempotency mechanism uses the transaction input file path and SHA-256 fingerprint.
- Account-file changes are not independently included in the current idempotency fingerprint.
- Malformed CSV handling, atomic output publishing, and expanded recovery scenarios are areas for future improvement.
