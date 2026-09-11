
public class TestAccountExceptions {

    // ============================================================
    // Helper method to display account details
    // ============================================================

    public static void displayAccount(Account account) {
        System.out.println(
            "Account #" + account.getAccountNumber()
            + " | " + account.getName()
            + " (" + account.getAge() + " yrs)"
            + " | " + account.getAccountType()
            + " | ₹" + account.getBalance()
            + " | " + account.getStatus()
            + " | PIN: " + (account.hasPin() ? "Yes" : "No")
        );
    }


    public static void main(String[] args) {

        // Store accounts that were successfully created
        Account[] accounts = new Account[7];
        int accountCount = 0;


        System.out.println("============================================================");
        System.out.println("             ACCOUNT TEST WITH EXCEPTIONS");
        System.out.println("============================================================");


        // ========================================================
        // TEST 1: Valid Account Creation
        // ========================================================

        System.out.println("\n>>> Test 1: Valid Account Creation");

        try {
            Account account1 = new Account(
                1001,
                "John Doe",
                25,
                1000.0,
                "Savings"
            );

            accounts[accountCount++] = account1;

            System.out.print("SUCCESS: ");
            displayAccount(account1);

        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }


        // ========================================================
        // TEST 2: Invalid Age
        // ========================================================

        System.out.println("\n>>> Test 2: Invalid Age (under 18)");

        try {
            Account account2 = new Account(
                1002,
                "Bob Smith",
                16,
                1000.0,
                "Savings"
            );

            System.out.println("SUCCESS: Account created.");

        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }


        // ========================================================
        // TEST 3: Invalid Account Type
        // ========================================================

        System.out.println("\n>>> Test 3: Invalid Account Type");

        try {
            Account account3 = new Account(
                1003,
                "Charlie Brown",
                25,
                1000.0,
                "Invalid"
            );

            System.out.println("SUCCESS: Account created.");

        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }


        // ========================================================
        // TEST 4: Minimum Balance on Creation
        // ========================================================

        System.out.println("\n>>> Test 4: Minimum Balance on Creation");
        System.out.println("\nCreating Savings account with ₹300");

        try {
            Account account4 = new Account(
                1004,
                "David Wilson",
                25,
                300.0,
                "Savings"
            );

            System.out.println("SUCCESS: Account created.");

        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }


        // ========================================================
        // TEST 5: Valid Deposit and Withdrawal
        // ========================================================

        System.out.println("\n>>> Test 5: Valid Deposit and Withdrawal");

        try {
            Account account5 = new Account(
                1005,
                "Alice Brown",
                30,
                1000.0,
                "Current"
            );

            accounts[accountCount++] = account5;

            System.out.print("Account: ");
            displayAccount(account5);

            // Set PIN
            account5.setPin(1234);
            System.out.println("Setting PIN 1234: SUCCESS");

            // Deposit
            System.out.println("Depositing ₹500.0: SUCCESS");
            account5.deposit(500.0);

            System.out.println(
                "Balance after deposit: ₹"
                + account5.getBalance()
            );

            // Withdrawal
            account5.withdraw(200.0, 1234);

            System.out.println("Withdrawing ₹200.0: SUCCESS");

            System.out.println(
                "Balance after withdrawal: ₹"
                + account5.getBalance()
            );

            displayAccount(account5);

        } catch (InvalidAmountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (InsufficientBalanceException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (InactiveAccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (InvalidPinException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (MinimumBalanceViolationException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }


        // ========================================================
        // TEST 6: Invalid Deposit
        // ========================================================

        System.out.println("\n>>> Test 6: Invalid Deposit (Negative Amount)");
        System.out.println("Attempting to deposit ₹-100.0");

        try {
            Account account6 = new Account(
                1006,
                "Charlie Green",
                35,
                500.0,
                "Savings"
            );

            account6.setPin(1234);
            accounts[accountCount++] = account6;

            account6.deposit(-100.0);

            System.out.println("SUCCESS: Deposit completed.");

        } catch (InvalidAmountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (InactiveAccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }


        // ========================================================
        // TEST 7: Insufficient Balance
        // ========================================================

        System.out.println("\n>>> Test 7: Insufficient Balance");

        try {
            Account account7 = new Account(
                1007,
                "Diana Prince",
                28,
                1000.0,
                "Savings"
            );

            account7.setPin(1234);
            accounts[accountCount++] = account7;

            System.out.print("Account: ");
            displayAccount(account7);

            System.out.println("Attempting to withdraw ₹1000.0");

            account7.withdraw(1000.0, 1234);

            System.out.println("SUCCESS: Withdrawal completed.");

        } catch (InsufficientBalanceException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (MinimumBalanceViolationException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (InvalidAmountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (InactiveAccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (InvalidPinException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }


        // ========================================================
        // TEST 8: Minimum Balance Violation
        // ========================================================

        System.out.println("\n>>> Test 8: Minimum Balance Violation");

        try {
            Account account8 = new Account(
                1008,
                "Eve Wilson",
                32,
                1000.0,
                "Savings"
            );

            account8.setPin(1234);

            // Replace account 1008 in summary
            accounts[accountCount++] = account8;

            System.out.print("Account: ");
            displayAccount(account8);

            System.out.println("Attempting to withdraw ₹600.0");

            account8.withdraw(600.0, 1234);

            System.out.println("SUCCESS: Withdrawal completed.");

        } catch (MinimumBalanceViolationException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (InsufficientBalanceException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (InvalidAmountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (InactiveAccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (InvalidPinException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }


        // ========================================================
        // TEST 9: Inactive Account Operations
        // ========================================================

        System.out.println("\n>>> Test 9: Inactive Account Operations");

        try {
            Account account9 = new Account(
                1009,
                "Eve Wilson",
                32,
                2000.0,
                "Current"
            );

            accounts[accountCount++] = account9;

            System.out.print("Account: ");
            displayAccount(account9);

            // Close account
            account9.closeAccount();
            System.out.println("Closing account: SUCCESS");

            // Try to deposit while inactive
            System.out.println(
                "Attempting to deposit ₹100.0 on closed account"
            );

            try {
                account9.deposit(100.0);

                System.out.println("Deposit: SUCCESS");

            } catch (InactiveAccountException e) {
                System.out.println("EXCEPTION: " + e.getMessage());
            }

            // Reopen
            account9.reopenAccount();
            System.out.println("Reopening account: SUCCESS");

            // Deposit after reopening
            account9.deposit(100.0);

            System.out.println(
                "Depositing ₹100.0 after reopen: SUCCESS"
            );

            System.out.println(
                "Balance after deposit: ₹"
                + account9.getBalance()
            );

        } catch (IllegalStateException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (InvalidAmountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (InactiveAccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }


        // ========================================================
        // TEST 10: PIN Verification
        // ========================================================

        System.out.println("\n>>> Test 10: PIN Verification");

        try {
            Account account10 = new Account(
                1009,
                "Frank Miller",
                40,
                1500.0,
                "Savings"
            );

            accounts[accountCount++] = account10;

            System.out.print("Account: ");
            displayAccount(account10);

            // Set PIN
            account10.setPin(1234);
            System.out.println("Setting PIN 1234: SUCCESS");

            // Correct PIN
            System.out.println(
                "Withdrawing ₹200.0 with correct PIN: SUCCESS"
            );

            account10.withdraw(200.0, 1234);

            System.out.println(
                "Balance: ₹"
                + account10.getBalance()
            );

            // Incorrect PIN
            System.out.println(
                "Attempting to withdraw ₹100.0 "
                + "with incorrect PIN (9999)"
            );

            try {
                account10.withdraw(100.0, 9999);

                System.out.println("SUCCESS: Withdrawal completed.");

            } catch (InvalidPinException e) {
                System.out.println("EXCEPTION: " + e.getMessage());
            }

        } catch (InvalidAmountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (InsufficientBalanceException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (InactiveAccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (InvalidPinException e) {
            System.out.println("EXCEPTION: " + e.getMessage());

        } catch (MinimumBalanceViolationException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }


        // ========================================================
        // TEST 11: All Accounts Summary
        // ========================================================

        System.out.println("\n>>> Test 11: All Accounts Summary");

        for (int i = 0; i < accounts.length; i++) {

            if (accounts[i] != null) {
                displayAccount(accounts[i]);
            }
        }


        // ========================================================
        // END
        // ========================================================

        System.out.println("\n============================================================");
        System.out.println("                    TEST COMPLETED!");
        System.out.println("============================================================");
    }
}


