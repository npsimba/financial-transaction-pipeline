
# Data Dictionary

## 1. Transaction Source Data – transactions.csv

The `transactions.csv` file contains individual financial transaction legs. Each transaction can consist of multiple debit and credit legs grouped using `transaction_id`.

| Column | Data Type | Description | Constraints |
|---|---|---|---|
| leg_id | String | Unique transaction leg identifier | Primary key, NOT NULL |
| event_id | String | Unique event identifier | UNIQUE, NOT NULL |
| transaction_id | String | Groups related transaction legs | NOT NULL |
| account_id | String | Account involved in the transaction | Foreign key, NOT NULL |
| leg_type | String | Type of transaction leg | DEBIT or CREDIT |
| amount | BigDecimal | Financial transaction amount | Must be positive |
| currency | String | Three-character currency code | NOT NULL |
| event_timestamp | LocalDateTime | Transaction event timestamp | ISO-8601 format, NOT NULL |
| event_sequence | Integer | Sequence of event within a transaction | Positive, unique per transaction |

## 2. Account Reference Data – accounts.csv

The `accounts.csv` file contains account reference information used to validate transaction account IDs and account status.

| Column | Data Type | Description | Constraints |
|---|---|---|---|
| account_id | String | Unique account identifier | Primary key, NOT NULL |
| account_status | String | Current account status | ACTIVE or INACTIVE |

## 3. Derived Metadata

The following metadata fields are added during pipeline processing.

| Field | Data Type | Description |
|---|---|---|
| run_id | String | Unique identifier for each pipeline execution |
| source_file | String | Original transaction input filename |
| rejection_reason | String | Explanation for a rejected transaction |
| processed_at | Timestamp | Time when the record was processed |

## 4. Target Outputs

### 4.1 Curated Transactions

Contains transaction legs belonging only to successfully validated transaction groups.

The output includes:

- Original transaction fields.
- run_id
- source_file
- processed_at

Only valid and balanced transaction groups are published.

### 4.2 Quarantined Transactions

Contains rejected transaction records along with their rejection reasons.

The output includes:

- Original transaction fields.
- rejection_reason
- run_id
- source_file
- processed_at

Rejected records are retained for investigation and traceability.

### 4.3 Pipeline Run Report

The pipeline generates a CSV execution report containing the following fields:

| Field | Description |
|---|---|
| run_id | Unique pipeline execution identifier |
| input_count | Total input transaction legs |
| accepted_count | Number of accepted transaction legs |
| rejected_count | Number of rejected transaction legs |
| error_count | Number of reconciliation errors |
| status | Final pipeline execution status |

## 5. Entity Relationships

- One transaction can contain multiple transaction legs.
- Each transaction leg belongs to one account.
- Each account can participate in multiple transactions.
- Each transaction leg has one unique leg ID and event ID.
- Multiple transaction legs are grouped using transaction_id.

### Relationship Summary

- Transaction to Transaction Legs: One-to-Many.
- Account to Transaction Legs: One-to-Many.

## 6. Business Constraints

1. Total debit amount must equal total credit amount for each transaction and currency.
2. All transaction legs within the same transaction must use the same currency.
3. Every referenced account must exist and have ACTIVE status.
4. Leg IDs and event IDs must be unique.
5. Transaction amounts must be positive.
6. Leg type must be either DEBIT or CREDIT.
7. Event sequence numbers must be unique within a transaction.
8. Event timestamps must follow the event sequence.
9. If a transaction fails group-level validation, all its legs are quarantined.
10. Invalid transaction groups must never enter curated outputs.
11. Required transaction fields must not be null or empty.

## 7. Data Processing and Integrity

- Financial calculations use BigDecimal to maintain decimal precision.
- Transaction groups are validated before publishing.
- Duplicate detection uses leg IDs and event IDs.
- SHA-256 input file fingerprints support idempotent processing.
- Reconciliation checks ensure input records are accounted for as accepted or rejected.
- Rejected records retain their rejection reasons for traceability.
- Run reports and application logs provide execution monitoring information.

## 8. Data Quality and Validation

The pipeline performs the following validation checks:

- Required field validation.
- Data type and amount validation.
- Account existence and account status validation.
- Duplicate leg and event detection.
- Currency consistency validation.
- Event sequence and timestamp ordering validation.
- Debit and credit balance validation.
- Accepted and rejected record reconciliation.

Invalid transaction groups are quarantined rather than published into curated outputs.

