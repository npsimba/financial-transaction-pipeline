
# Data Dictionary

## 1. Transaction Data – transactions.csv

| Column | Data Type | Description | Constraints |
|---|---|---|---|
| leg_id | String | Unique transaction leg ID | Primary key, NOT NULL |
| event_id | String | Unique event ID | UNIQUE, NOT NULL |
| transaction_id | String | Groups related transaction legs | NOT NULL |
| account_id | String | Account involved in the transaction | Foreign key, NOT NULL |
| leg_type | String | DEBIT or CREDIT | NOT NULL |
| amount | BigDecimal | Transaction amount | Must be positive |
| currency | String | Currency code such as INR or USD | NOT NULL |
| event_timestamp | ISO-8601 timestamp | Actual event time | NOT NULL |
| event_sequence | Integer | Sequence of event within transaction | Unique per transaction |

## 2. Account Reference Data – accounts.csv

| Column | Data Type | Description | Constraints |
|---|---|---|---|
| account_id | String | Unique account identifier | Primary key |
| account_status | String | Account status | ACTIVE or INACTIVE |

## 3. Derived Metadata

| Field | Description |
|---|---|
| run_id | Unique identifier for each pipeline execution |
| source_file | Original input filename |
| rejection_reason | Explanation for a rejected record |
| processed_at | Time when the pipeline processed the record |

## 4. Target Outputs

### Curated Transactions

Contains only records belonging to successfully validated transactions.

### Quarantined Transactions

Contains rejected records and their rejection reasons.

### Pipeline Run Report

Contains run ID, start time, end time, input count, accepted count, rejected count, and execution status.

## 5. Relationships

- One transaction can contain multiple transaction legs.
- Each transaction leg belongs to one account.
- Each account can participate in multiple transactions.
- Each transaction leg has one unique event ID.
- Multiple legs are grouped using transaction_id.

## 6. Business Constraints

1. Debit total must equal credit total for each transaction and currency.
2. All legs within a transaction must use the same currency.
3. Every account ID must exist in accounts.csv.
4. Leg IDs and event IDs must be unique.
5. Event sequence must be unique within a transaction.
6. Event timestamps must follow event sequence.
7. If a transaction fails group-level validation, all its legs are quarantined.
