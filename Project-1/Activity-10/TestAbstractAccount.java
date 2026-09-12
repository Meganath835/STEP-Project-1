
// ============================================================
// ACTIVITY 10: TESTING ABSTRACT ACCOUNT
//
// Integration test driver that exercises the Account hierarchy
// polymorphically -- every account under test is held through an
// `Account` reference, so only the abstract contract
// (getAccountType, getMinimumBalance, calculateInterest,
// displayAccountInfo) is ever called directly.
//
// The domain classes (Account, SavingsAccount, CurrentAccount,
// FixedDepositAccount) live once in ../common/bank and are
// imported here rather than copy-pasted.
// ============================================================

import bank.*;

public class TestAbstractAccount {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        System.out.println("=== Activity 10: Testing Abstract Account ===\n");

        // ------------------------------------------------------
        // Build three different subclasses, but reference each
        // one through the abstract Account type.
        // ------------------------------------------------------

        Account sa = new SavingsAccount(901, "Alice", 25, 10000.0);
        Account ca = new CurrentAccount(902, "Bob", 35, 50000.0);
        Account fd = new FixedDepositAccount(903, "Charlie", 40, 100000.0, 1);

        Account[] accounts = { sa, ca, fd };

        System.out.println(">>> displayAccountInfo() via abstract references");
        for (Account account : accounts) {
            account.displayAccountInfo();
            System.out.println("---");
        }

        // ------------------------------------------------------
        // Test: interest calculation, dispatched polymorphically
        // ------------------------------------------------------

        System.out.println("\n>>> Interest calculation (polymorphic)");
        assertEquals("SavingsAccount.calculateInterest(1)", 400.0, sa.calculateInterest(1));
        assertEquals("CurrentAccount.calculateInterest(1)", 0.0, ca.calculateInterest(1));
        assertEquals("FixedDepositAccount.calculateInterest(1)", 7500.0, fd.calculateInterest(1));

        // ------------------------------------------------------
        // Test: minimum balance contract, dispatched polymorphically
        // ------------------------------------------------------

        System.out.println("\n>>> Minimum balance assertions (polymorphic)");
        assertEquals("SavingsAccount.getMinimumBalance()", 500.0, sa.getMinimumBalance());
        assertEquals("CurrentAccount.getMinimumBalance()", 1000.0, ca.getMinimumBalance());
        assertEquals("FixedDepositAccount.getMinimumBalance()", 5000.0, fd.getMinimumBalance());

        // ------------------------------------------------------
        // Test: account type contract, dispatched polymorphically
        // ------------------------------------------------------

        System.out.println("\n>>> Account type assertions (polymorphic)");
        assertEquals("SavingsAccount.getAccountType()", "Savings", sa.getAccountType());
        assertEquals("CurrentAccount.getAccountType()", "Current", ca.getAccountType());
        assertEquals("FixedDepositAccount.getAccountType()", "Fixed Deposit", fd.getAccountType());

        // ------------------------------------------------------
        // Summary
        // ------------------------------------------------------

        System.out.println("\n============================================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed");
        System.out.println("============================================================");

        if (failed > 0) {
            throw new AssertionError(failed + " assertion(s) failed");
        }

        System.out.println("Template method pattern executed successfully!");
    }


    // ------------------------------------------------------------
    // Small assertion helper -- prints PASS/FAIL for every check
    // instead of relying on the `assert` keyword (which is disabled
    // unless the JVM is run with -ea).
    // ------------------------------------------------------------

    private static void assertEquals(String label, double expected, double actual) {
        if (Double.compare(expected, actual) == 0) {
            passed++;
            System.out.println("PASS: " + label + " == " + expected);
        } else {
            failed++;
            System.out.println(
                "FAIL: " + label + " expected " + expected + " but got " + actual
            );
        }
    }

    private static void assertEquals(String label, String expected, String actual) {
        if (expected.equals(actual)) {
            passed++;
            System.out.println("PASS: " + label + " == \"" + expected + "\"");
        } else {
            failed++;
            System.out.println(
                "FAIL: " + label + " expected \"" + expected + "\" but got \"" + actual + "\""
            );
        }
    }
}
