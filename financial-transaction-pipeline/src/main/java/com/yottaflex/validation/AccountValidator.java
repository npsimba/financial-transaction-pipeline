
package com.yottaflex.validation;

import com.yottaflex.model.Account;

import java.util.ArrayList;
import java.util.HashMap;

public class AccountValidator {

    private HashMap<String, String> accountStatusMap = new HashMap<>();

    public void loadAccounts(ArrayList<Account> accounts) {

        accountStatusMap.clear();

        for (Account account : accounts) {
            accountStatusMap.put(
                    account.getAccountId(),
                    account.getAccountStatus()
            );
        }
    }

    public String validateAccount(String accountId) {

        if (accountId == null || accountId.trim().isEmpty()) {
            return "MISSING_ACCOUNT_ID";
        }

        if (!accountStatusMap.containsKey(accountId)) {
            return "ACCOUNT_NOT_FOUND";
        }

        if (!"ACTIVE".equalsIgnoreCase(accountStatusMap.get(accountId))) {
            return "ACCOUNT_INACTIVE";
        }

        return null;
    }
}
