
// ============================================================
// ACTIVITY 12: TESTING INTERFACE AND FACTORY
//
// End-to-end integration test driver. It verifies that client
// code can interact with EVERY account type exclusively through
// the IAccount interface and AccountFactory -- no concrete
// class (SavingsAccount, CurrentAccount, FixedDepositAccount,
// SalaryAccount) is ever named or imported here.
//
// The domain classes live once in ../common/bank and are
// imported here rather than copy-pasted.
// ============================================================

import bank.*;

public class TestInterfaceFactory {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        System.out.println("============================================================");
        System.out.println("   ACTIVITY 12: TESTING INTERFACE AND FACTORY");
        System.out.println("============================================================");

        // =========================================================
        // STEP 1: Instantiate all four account types purely through
        // AccountFactory.createAccount() -- every reference is typed
        // as IAccount.
        // =========================================================
        System.out.println("\n>>> Step 1: Creating all account types via AccountFactory");

        IAccount savings = AccountFactory.createAccount(
            "savings", 1201, "Alice", 25, 10000.0
        );

        IAccount current = AccountFactory.createAccount(
            "current", 1202, "Bob", 35, 50000.0
        );

        IAccount fixedDeposit = AccountFactory.createAccount(
            "fixeddeposit", 1203, "Charlie", 40, 20000.0, 2
        );

        IAccount salary = AccountFactory.createAccount(
            "salary", 1204, "Diana", 30, 5000.0, "Acme Corp"
        );

        IAccount[] accounts = { savings, current, fixedDeposit, salary };

        for (IAccount account : accounts) {
            System.out.println("Created: " + account.getAccountType());
        }


        // =========================================================
        // STEP 2: Exercise methods and assert properties purely
        // through interface methods.
        // =========================================================
        System.out.println("\n>>> Step 2a: Account type assertions (via IAccount)");

        assertEquals("savings.getAccountType()", "Savings", savings.getAccountType());
        assertEquals("current.getAccountType()", "Current", current.getAccountType());
        assertEquals("fixedDeposit.getAccountType()", "Fixed Deposit", fixedDeposit.getAccountType());
        assertEquals("salary.getAccountType()", "Salary", salary.getAccountType());

        System.out.println("\n>>> Step 2b: Minimum balance assertions (via IAccount)");

        assertEquals("savings.getMinimumBalance()", 500.0, savings.getMinimumBalance());
        assertEquals("current.getMinimumBalance()", 1000.0, current.getMinimumBalance());
        assertEquals("fixedDeposit.getMinimumBalance()", 5000.0, fixedDeposit.getMinimumBalance());
        assertEquals("salary.getMinimumBalance()", 0.0, salary.getMinimumBalance());

        System.out.println("\n>>> Step 2c: Interest calculation assertions (via IAccount)");

        assertEquals("savings.calculateInterest(1)", 400.0, savings.calculateInterest(1));
        assertEquals("current.calculateInterest(1)", 0.0, current.calculateInterest(1));
        assertEquals("fixedDeposit.calculateInterest(1)", 3000.0, fixedDeposit.calculateInterest(1));
        assertEquals("salary.calculateInterest(1)", 150.0, salary.calculateInterest(1));

        System.out.println("\n>>> Step 2d: Balance/account-number/status assertions (via IAccount)");

        assertEquals("savings.getBalance()", 10000.0, savings.getBalance());
        assertEquals("savings.getAccountNumber()", 1201, savings.getAccountNumber());
        assertEquals("savings.getStatus()", "Active", savings.getStatus());


        // =========================================================
        // STEP 3: Deposit / withdraw purely through the interface.
        // =========================================================
        System.out.println("\n>>> Step 3: Deposit/Withdraw via IAccount");

        try {
            savings.deposit(500.0);
            assertEquals("savings.getBalance() after deposit", 10500.0, savings.getBalance());

            savings.setPin(1111);
            savings.withdraw(1000.0, 1111);
            assertEquals("savings.getBalance() after withdraw", 9500.0, savings.getBalance());

        } catch (AccountException e) {
            fail("Unexpected exception during savings deposit/withdraw: " + e.getMessage());
        }

        try {
            current.deposit(1000.0);
            assertEquals("current.getBalance() after deposit", 51000.0, current.getBalance());

        } catch (AccountException e) {
            fail("Unexpected exception during current deposit: " + e.getMessage());
        }


        // =========================================================
        // STEP 4: Exception paths, still only through IAccount.
        // =========================================================
        System.out.println("\n>>> Step 4: Exception handling via IAccount");

        // Fixed deposit rejects deposits outright.
        try {
            fixedDeposit.deposit(100.0);
            fail("fixedDeposit.deposit() should have thrown");
        } catch (UnsupportedOperationException e) {
            pass("fixedDeposit.deposit() correctly rejected: " + e.getMessage());
        } catch (AccountException e) {
            fail("Wrong exception type from fixedDeposit.deposit(): " + e.getMessage());
        }

        // Fixed deposit rejects withdrawal before maturity.
        try {
            fixedDeposit.withdraw(100.0, 0);
            fail("fixedDeposit.withdraw() before maturity should have thrown");
        } catch (IllegalStateException e) {
            pass("fixedDeposit.withdraw() before maturity correctly rejected: " + e.getMessage());
        } catch (AccountException e) {
            fail("Wrong exception type from fixedDeposit.withdraw(): " + e.getMessage());
        }

        // Savings rejects a withdrawal that breaches minimum balance.
        try {
            savings.withdraw(9200.0, 1111);
            fail("savings.withdraw() below minimum balance should have thrown");
        } catch (MinimumBalanceViolationException e) {
            pass("savings.withdraw() below minimum correctly rejected: " + e.getMessage());
        } catch (AccountException e) {
            fail("Wrong exception type from savings.withdraw(): " + e.getMessage());
        }

        // Deposits are rejected once an account is closed.
        current.closeAccount();
        try {
            current.deposit(100.0);
            fail("current.deposit() on closed account should have thrown");
        } catch (InactiveAccountException e) {
            pass("current.deposit() on closed account correctly rejected: " + e.getMessage());
        } catch (AccountException e) {
            fail("Wrong exception type from current.deposit(): " + e.getMessage());
        }
        current.reopenAccount();
        assertEquals("current.getStatus() after reopen", "Active", current.getStatus());


        // =========================================================
        // STEP 5: Polymorphic display -- one loop, four account
        // types, all through IAccount.displayAccountInfo().
        // =========================================================
        System.out.println("\n>>> Step 5: Polymorphic displayAccountInfo() via IAccount");

        for (IAccount account : accounts) {
            account.displayAccountInfo();
            System.out.println("---");
        }


        // =========================================================
        // STEP 6: Unknown account type is rejected by the factory.
        // =========================================================
        System.out.println("\n>>> Step 6: AccountFactory rejects unknown types");

        try {
            AccountFactory.createAccount("bitcoin", 9999, "Nobody", 30, 100.0);
            fail("AccountFactory.createAccount() with an unknown type should have thrown");
        } catch (IllegalArgumentException e) {
            pass("AccountFactory correctly rejected unknown type: " + e.getMessage());
        }


        // =========================================================
        // Summary
        // =========================================================
        System.out.println("\n============================================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed");
        System.out.println("============================================================");

        if (failed > 0) {
            throw new AssertionError(failed + " assertion(s) failed");
        }

        System.out.println("Interface + Factory pattern verified end-to-end!");
    }


    // ------------------------------------------------------------
    // Assertion helpers
    // ------------------------------------------------------------

    private static void pass(String message) {
        passed++;
        System.out.println("PASS: " + message);
    }

    private static void fail(String message) {
        failed++;
        System.out.println("FAIL: " + message);
    }

    private static void assertEquals(String label, double expected, double actual) {
        if (Double.compare(expected, actual) == 0) {
            pass(label + " == " + expected);
        } else {
            fail(label + " expected " + expected + " but got " + actual);
        }
    }

    private static void assertEquals(String label, int expected, int actual) {
        if (expected == actual) {
            pass(label + " == " + expected);
        } else {
            fail(label + " expected " + expected + " but got " + actual);
        }
    }

    private static void assertEquals(String label, String expected, String actual) {
        if (expected.equals(actual)) {
            pass(label + " == \"" + expected + "\"");
        } else {
            fail(label + " expected \"" + expected + "\" but got \"" + actual + "\"");
        }
    }
}
