
package com.yottaflex.ingestion;

import com.yottaflex.model.Transaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TransactionReader {

    public ArrayList<Transaction> readTransactions(String filePath) throws Exception {

        ArrayList<Transaction> transactions = new ArrayList<>();

        Reader reader = new FileReader(filePath);
        CSVParser parser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(reader);

        for (CSVRecord record : parser) {

            Transaction transaction = new Transaction(
                    record.get("leg_id"),
                    record.get("event_id"),
                    record.get("transaction_id"),
                    record.get("account_id"),
                    record.get("leg_type"),
                    new BigDecimal(record.get("amount")),
                    record.get("currency"),
                    LocalDateTime.parse(record.get("event_timestamp")),
                    Integer.parseInt(record.get("event_sequence"))
            );

            transactions.add(transaction);
        }

        parser.close();
        reader.close();

        return transactions;
    }
}
