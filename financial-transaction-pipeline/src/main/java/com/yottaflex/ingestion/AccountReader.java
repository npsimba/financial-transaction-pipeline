
package com.yottaflex.ingestion;

import com.yottaflex.model.Account;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

public class AccountReader {

    public ArrayList<Account> readAccounts(String filePath) throws Exception {

        ArrayList<Account> accounts = new ArrayList<>();

        Reader reader = new FileReader(filePath);

        CSVParser parser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(reader);

        for (CSVRecord record : parser) {

            Account account = new Account(
                    record.get("account_id"),
                    record.get("account_status")
            );

            accounts.add(account);
        }

        parser.close();
        reader.close();

        return accounts;
    }
}
