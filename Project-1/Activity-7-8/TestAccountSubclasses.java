
public class TestAccountSubclasses {

    // Helper method to display an account
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

        System.out.println("============================================================");
        System.out.println("      ACCOUNT SUBCLASSES TEST (SAVINGS & CURRENT)");
        System.out.println("============================================================");

        // =========================================================
        // TEST 1: Creating Accounts
        // =========================================================
        System.out.println("\n>>> Test 1: Creating Accounts");

        SavingsAccount savings1 =
            new SavingsAccount(1001, "John Doe", 25, 1000);

        CurrentAccount current1 =
            new CurrentAccount(1002, "Jane Smith", 30, 2000);

        System.out.print("Savings Account: ");
        displayAccount(savings1);

        System.out.print("Current Account: ");
        displayAccount(current1);


        // =========================================================
        // TEST 2: Account Type and Minimum Balance
        // =========================================================
        System.out.println("\n>>> Test 2: Account Type and Minimum Balance");

        System.out.println(
            "Savings Account - Type: "
            + savings1.getAccountType()
            + ", Minimum Balance: ₹"
            + savings1.getMinimumBalance()
        );

        System.out.println(
            "Current Account - Type: "
            + current1.getAccountType()
            + ", Minimum Balance: ₹"
            + current1.getMinimumBalance()
        );


        // =========================================================
        // TEST 3: Savings Account - Interest Calculation
        // =========================================================
        System.out.println("\n>>> Test 3: Savings Account - Interest Calculation");

        System.out.print("Savings Account: ");
        displayAccount(savings1);

        System.out.println(
            "Interest Rate: "
            + savings1.getInterestRate()
            + "% per annum"
        );

        System.out.println(
            "Interest for 1 year: ₹"
            + savings1.calculateInterest(1)
        );

        System.out.println(
            "Interest for 2 years: ₹"
            + savings1.calculateInterest(2)
        );

        System.out.println(
            "Interest for 5 years: ₹"
            + savings1.calculateInterest(5)
        );

        double interest2Years = savings1.calculateInterest(2);

        System.out.println(
            "After 2 years with interest: Balance would be ₹"
            + (savings1.getBalance() + interest2Years)
        );


        // =========================================================
        // TEST 4: Current Account - Overdraft Feature
        // =========================================================
        System.out.println("\n>>> Test 4: Current Account - Overdraft Feature");

        System.out.print("Current Account: ");
        displayAccount(current1);

        System.out.println(
            "Overdraft Limit: ₹"
            + current1.getOverdraftLimit()
        );

        System.out.println(
            "Available Overdraft: ₹"
            + current1.getAvailableOverdraft()
        );

        System.out.println(
            "Overdraft Used: ₹"
            + current1.getOverdraftUsed()
        );

        System.out.println(
            "Is Using Overdraft: "
            + current1.isUsingOverdraft()
        );

        System.out.println(
            "Withdrawing ₹1500.0 "
            + "(goes below minimum balance of ₹1000)"
        );

        System.out.println(
            "Balance before: ₹"
            + current1.getBalance()
        );

        try {
            current1.withdraw(1500, 0);

            System.out.println(
                "Withdrawing: ₹1500.0 - SUCCESS"
            );

            System.out.println(
                "Balance after: ₹"
                + current1.getBalance()
            );

            System.out.println(
                "Overdraft Used: ₹"
                + current1.getOverdraftUsed()
            );

            System.out.println(
                "Available Overdraft: ₹"
                + current1.getAvailableOverdraft()
            );

            System.out.println(
                "Is Using Overdraft: "
                + current1.isUsingOverdraft()
            );

        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }


        // Attempt withdrawal that exceeds available funds
        System.out.println(
            "Attempting to withdraw ₹4000.0 "
            + "(would exceed overdraft)"
        );

        System.out.println(
            "Available funds: ₹"
            + current1.getBalance()
            + " (balance) + ₹"
            + current1.getAvailableOverdraft()
            + " (overdraft) = ₹"
            + (current1.getBalance()
               + current1.getAvailableOverdraft())
        );

        try {
            current1.withdraw(4000, 0);

            System.out.println(
                "Withdrawing: ₹4000.0 - SUCCESS"
            );

        } catch (AccountException e) {
            System.out.println(
                "EXCEPTION: " + e.getMessage()
            );
        }


        // Repay overdraft
        System.out.println("Repaying overdraft of ₹500.0");

        System.out.println(
            "Balance before repayment: ₹"
            + current1.getBalance()
        );

        System.out.println(
            "Overdraft Used before: ₹"
            + current1.getOverdraftUsed()
        );

        try {
            current1.repayOverdraft(500);

            System.out.println(
                "Repaying ₹500.0 - SUCCESS"
            );

            System.out.println(
                "Balance after repayment: ₹"
                + current1.getBalance()
            );

            System.out.println(
                "Overdraft Used after: ₹"
                + current1.getOverdraftUsed()
            );

            System.out.println(
                "Is Using Overdraft: "
                + current1.isUsingOverdraft()
            );

        } catch (IllegalArgumentException e) {
            System.out.println(
                "EXCEPTION: " + e.getMessage()
            );
        }


        // =========================================================
        // TEST 5: Polymorphism
        // =========================================================
        System.out.println(
            "\n>>> Test 5: Polymorphism - Treating Accounts Uniformly"
        );

        SavingsAccount savings2 =
            new SavingsAccount(1003, "Bob Wilson", 35, 500);

        CurrentAccount current2 =
            new CurrentAccount(1004, "Alice Brown", 28, 1500);

        Account[] accounts = {
            savings1,
            current1,
            savings2,
            current2
        };

        System.out.println(
            "Processing accounts polymorphically:"
        );

        double totalBalance = 0;

        for (Account account : accounts) {

            System.out.println(
                "Account #" + account.getAccountNumber()
                + " | " + account.getName()
                + " (" + account.getAge() + " yrs)"
                + " | " + account.getAccountType()
                + " | ₹" + account.getBalance()
                + " | " + account.getStatus()
                + " | Type: " + account.getAccountType()
                + ", Min Balance: ₹"
                + account.getMinimumBalance()
            );

            totalBalance += account.getBalance();
        }

        System.out.println(
            "Total accounts: " + accounts.length
        );

        System.out.println(
            "Total balance across all accounts: ₹"
            + totalBalance
        );


        // =========================================================
        // TEST 6: Validation - Invalid Creation Attempts
        // =========================================================
        System.out.println(
            "\n>>> Test 6: Validation - Invalid Creation Attempts"
        );

        System.out.println(
            "Attempting to create SavingsAccount with ₹300 "
            + "(below minimum)"
        );

        try {
            SavingsAccount invalidSavings =
                new SavingsAccount(
                    2001,
                    "Invalid Savings",
                    25,
                    300
                );

        } catch (IllegalArgumentException e) {
            System.out.println(
                "EXCEPTION: " + e.getMessage()
            );
        }


        System.out.println(
            "Attempting to create CurrentAccount with ₹500 "
            + "(below minimum)"
        );

        try {
            CurrentAccount invalidCurrent =
                new CurrentAccount(
                    2002,
                    "Invalid Current",
                    25,
                    500
                );

        } catch (IllegalArgumentException e) {
            System.out.println(
                "EXCEPTION: " + e.getMessage()
            );
        }


        System.out.println(
            "Attempting to create SavingsAccount with age 16"
        );

        try {
            SavingsAccount invalidAge =
                new SavingsAccount(
                    2003,
                    "Young Customer",
                    16,
                    1000
                );

        } catch (IllegalArgumentException e) {
            System.out.println(
                "EXCEPTION: " + e.getMessage()
            );
        }


        // =========================================================
        // TEST 7: Savings Account - PIN and Operations
        // =========================================================
        System.out.println(
            "\n>>> Test 7: Savings Account - PIN and Operations"
        );

        SavingsAccount savings3 =
            new SavingsAccount(
                1005,
                "Charlie Green",
                40,
                2000
            );

        System.out.print("Savings Account: ");
        displayAccount(savings3);

        System.out.println(
            "Setting PIN 1234: SUCCESS"
        );

        try {
            savings3.setPin(1234);

            System.out.println(
                "Depositing ₹500.0: SUCCESS"
            );

            savings3.deposit(500);

            System.out.println(
                "Balance after deposit: ₹"
                + savings3.getBalance()
            );

            savings3.withdraw(300, 1234);

            System.out.println(
                "Withdrawing ₹300.0 with correct PIN: SUCCESS"
            );

            System.out.println(
                "Balance after withdrawal: ₹"
                + savings3.getBalance()
            );

        } catch (AccountException e) {
            System.out.println(
                "EXCEPTION: " + e.getMessage()
            );
        }

        System.out.println(
            "Attempting to withdraw ₹2000.0 "
            + "(would violate minimum balance)"
        );

        try {
            savings3.withdraw(2000, 1234);

            System.out.println(
                "Withdrawing ₹2000.0 - SUCCESS"
            );

        } catch (AccountException e) {
            System.out.println(
                "EXCEPTION: " + e.getMessage()
            );
        }


        // =========================================================
        // TEST 8: Current Account - Active Status Operations
        // =========================================================
        System.out.println(
            "\n>>> Test 8: Current Account - Active Status Operations"
        );

        CurrentAccount current3 =
            new CurrentAccount(
                1006,
                "Diana Prince",
                35,
                3000
            );

        System.out.print("Current Account: ");
        displayAccount(current3);

        System.out.println("Closing account: SUCCESS");

        try {
            current3.closeAccount();

            System.out.println(
                "Attempting to deposit ₹100.0 on closed account"
            );

            try {
                current3.deposit(100);

            } catch (AccountException e) {
                System.out.println(
                    "EXCEPTION: " + e.getMessage()
                );
            }

            System.out.println("Reopening account: SUCCESS");

            current3.reopenAccount();

            System.out.println(
                "Depositing ₹100.0 after reopen: SUCCESS"
            );

            current3.deposit(100);

            System.out.println(
                "Balance after deposit: ₹"
                + current3.getBalance()
            );

        } catch (IllegalStateException e) {
            System.out.println(
                "EXCEPTION: " + e.getMessage()
            );

        } catch (AccountException e) {
            System.out.println(
                "EXCEPTION: " + e.getMessage()
            );
        }


        // =========================================================
        // TEST 9: All Accounts Summary
        // =========================================================
        System.out.println(
            "\n>>> Test 9: All Accounts Summary"
        );

        for (Account account : accounts) {
            displayAccount(account);
        }

        displayAccount(savings3);
        displayAccount(current3);


        // =========================================================
        // COMPLETED
        // =========================================================
        System.out.println(
            "\n============================================================"
        );

        System.out.println(
            "                  TEST COMPLETED!"
        );

        System.out.println(
            "============================================================"
        );
    }
}

