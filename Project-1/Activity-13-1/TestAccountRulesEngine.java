
// ============================================================
// ACTIVITY 13.1: RULES ENGINE (IF-ELSE)
//
// Verifies that AccountRulesEngine correctly centralizes the
// policy thresholds (minimum balance, interest rate, overdraft
// limit) that used to live as magic numbers inside each account
// subclass, and that validateWithdrawal() combines them
// correctly.
//
// AccountRulesEngine lives once in ../common/bank and is
// imported here rather than copy-pasted.
// ============================================================

import bank.*;

public class TestAccountRulesEngine {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        System.out.println("============================================================");
        System.out.println("   ACTIVITY 13.1: RULES ENGINE (IF-ELSE) TEST");
        System.out.println("============================================================");

        // =========================================================
        // STEP 1: Minimum balance rules
        // =========================================================
        System.out.println("\n>>> Step 1: Minimum Balance Rules");

        assertEquals("getMinimumBalance(\"SAVINGS\")", 1000.0,
            AccountRulesEngine.getMinimumBalance("SAVINGS"));

        assertEquals("getMinimumBalance(\"CURRENT\")", 0.0,
            AccountRulesEngine.getMinimumBalance("CURRENT"));

        assertEquals("getMinimumBalance(\"FIXEDDEPOSIT\")", 0.0,
            AccountRulesEngine.getMinimumBalance("FIXEDDEPOSIT"));

        assertEquals("getMinimumBalance(\"SALARY\")", 0.0,
            AccountRulesEngine.getMinimumBalance("SALARY"));


        // =========================================================
        // STEP 2: Interest rate & overdraft rules
        // =========================================================
        System.out.println("\n>>> Step 2: Interest Rate & Overdraft Rules");

        assertEquals("getInterestRate(\"SAVINGS\")", 4.0,
            AccountRulesEngine.getInterestRate("SAVINGS"));

        assertEquals("getInterestRate(\"FIXEDDEPOSIT\")", 6.5,
            AccountRulesEngine.getInterestRate("FIXEDDEPOSIT"));

        assertEquals("getInterestRate(\"CURRENT\")", 0.0,
            AccountRulesEngine.getInterestRate("CURRENT"));

        assertEquals("getInterestRate(\"SALARY\")", 0.0,
            AccountRulesEngine.getInterestRate("SALARY"));

        assertEquals("getOverdraftLimit(\"CURRENT\")", 10000.0,
            AccountRulesEngine.getOverdraftLimit("CURRENT"));

        assertEquals("getOverdraftLimit(\"SAVINGS\")", 0.0,
            AccountRulesEngine.getOverdraftLimit("SAVINGS"));

        assertEquals("getOverdraftLimit(\"FIXEDDEPOSIT\")", 0.0,
            AccountRulesEngine.getOverdraftLimit("FIXEDDEPOSIT"));


        // =========================================================
        // STEP 2b: Rules engine accepts the same account-type
        // strings the rest of the codebase already uses -- the
        // factory's lowercase keys and the display strings
        // returned by IAccount.getAccountType().
        // =========================================================
        System.out.println("\n>>> Step 2b: Normalization Across Naming Styles");

        assertEquals("getMinimumBalance(\"savings\")", 1000.0,
            AccountRulesEngine.getMinimumBalance("savings"));

        assertEquals("getInterestRate(\"Fixed Deposit\")", 6.5,
            AccountRulesEngine.getInterestRate("Fixed Deposit"));

        assertEquals("getOverdraftLimit(\"current\")", 10000.0,
            AccountRulesEngine.getOverdraftLimit("current"));


        // =========================================================
        // STEP 3: Withdrawal rule validation
        // =========================================================
        System.out.println("\n>>> Step 3: Withdrawal Rule Validation");

        // Savings: min balance 1000, no overdraft.
        // Balance 5000, withdraw 3000 -> remaining 2000 >= 1000 -> allowed.
        assertTrue("SAVINGS withdraw within minimum balance",
            AccountRulesEngine.validateWithdrawal("SAVINGS", 5000.0, 3000.0));

        // Savings: balance 5000, withdraw 4500 -> remaining 500 < 1000 -> rejected.
        assertFalse("SAVINGS withdraw breaching minimum balance",
            AccountRulesEngine.validateWithdrawal("SAVINGS", 5000.0, 4500.0));

        // Current: no minimum balance, 10000 overdraft.
        // Balance 2000, withdraw 11000 -> remaining -9000 >= -10000 -> allowed.
        assertTrue("CURRENT withdraw using overdraft",
            AccountRulesEngine.validateWithdrawal("CURRENT", 2000.0, 11000.0));

        // Current: balance 2000, withdraw 13000 -> remaining -11000 < -10000 -> rejected.
        assertFalse("CURRENT withdraw exceeding overdraft",
            AccountRulesEngine.validateWithdrawal("CURRENT", 2000.0, 13000.0));

        // Fixed Deposit: no minimum balance, no overdraft.
        // Balance 10000, withdraw 10000 -> remaining 0 >= 0 -> allowed.
        assertTrue("FIXEDDEPOSIT withdraw down to zero",
            AccountRulesEngine.validateWithdrawal("FIXEDDEPOSIT", 10000.0, 10000.0));

        // Fixed Deposit: balance 10000, withdraw 10000.01 -> remaining < 0 -> rejected.
        assertFalse("FIXEDDEPOSIT overdraw",
            AccountRulesEngine.validateWithdrawal("FIXEDDEPOSIT", 10000.0, 10000.01));

        // Non-positive withdrawal amounts are always rejected.
        assertFalse("Non-positive withdrawal amount rejected",
            AccountRulesEngine.validateWithdrawal("CURRENT", 5000.0, 0.0));


        // =========================================================
        // Summary
        // =========================================================
        System.out.println("\n============================================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed");
        System.out.println("============================================================");

        if (failed > 0) {
            throw new AssertionError(failed + " assertion(s) failed");
        }

        System.out.println("AccountRulesEngine verified successfully!");
    }


    // ------------------------------------------------------------
    // Assertion helpers
    // ------------------------------------------------------------

    private static void assertEquals(String label, double expected, double actual) {
        if (Double.compare(expected, actual) == 0) {
            passed++;
            System.out.println("PASS: " + label + " == " + expected);
        } else {
            failed++;
            System.out.println("FAIL: " + label + " expected " + expected + " but got " + actual);
        }
    }

    private static void assertTrue(String label, boolean actual) {
        if (actual) {
            passed++;
            System.out.println("PASS: " + label + " -> allowed");
        } else {
            failed++;
            System.out.println("FAIL: " + label + " expected allowed but was rejected");
        }
    }

    private static void assertFalse(String label, boolean actual) {
        if (!actual) {
            passed++;
            System.out.println("PASS: " + label + " -> rejected");
        } else {
            failed++;
            System.out.println("FAIL: " + label + " expected rejected but was allowed");
        }
    }
}
