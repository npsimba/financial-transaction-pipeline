
package com.yottaflex.validation;

import com.yottaflex.model.Account;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AccountValidatorTest {

    @Test
    public void testActiveAccount() {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(new Account("ACC001", "ACTIVE"));

        AccountValidator validator = new AccountValidator();
        validator.loadAccounts(accounts);

        assertNull(validator.validateAccount("ACC001"));
    }

    @Test
    public void testMissingAccount() {
        AccountValidator validator = new AccountValidator();

        assertEquals(
                "MISSING_ACCOUNT_ID",
                validator.validateAccount("")
        );
    }

    @Test
    public void testAccountNotFound() {
        AccountValidator validator = new AccountValidator();

        assertEquals(
                "ACCOUNT_NOT_FOUND",
                validator.validateAccount("ACC999")
        );
    }

    @Test
    public void testInactiveAccount() {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(new Account("ACC002", "INACTIVE"));

        AccountValidator validator = new AccountValidator();
        validator.loadAccounts(accounts);

        assertEquals(
                "ACCOUNT_INACTIVE",
                validator.validateAccount("ACC002")
        );
    }
}
