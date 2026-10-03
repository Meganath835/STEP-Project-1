
// ============================================================
// ACTIVITY 13.2: RULES ENGINE INTEGRATION


import bank.*;

public class TestRulesEngineIntegration {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        System.out.println("============================================================");
        System.out.println("   ACTIVITY 13.2: RULES ENGINE INTEGRATION TEST");
        System.out.println("============================================================");

        
        // STEP 1: Same policy values as Activity 13.1, now served
        // out of the dictionary instead of if/else branches.
        
        System.out.println("\n>>> Step 1: Dictionary-backed lookups match the known policies");

        String[] products = { "SAVINGS", "CURRENT", "FIXEDDEPOSIT", "SALARY" };

        double[][] expected = {
            // minBalance, interestRate, overdraftLimit
            { 1000.0, 4.0, 0.0 },   // SAVINGS
            { 0.0, 0.0, 10000.0 },  // CURRENT
            { 0.0, 6.5, 0.0 },      // FIXEDDEPOSIT
            { 0.0, 0.0, 0.0 }       // SALARY (falls back to default policy)
        };

        for (int i = 0; i < products.length; i++) {
            String product = products[i];

            assertEquals(product + " minimum balance", expected[i][0],
                AccountRulesEngine.getMinimumBalance(product));

            assertEquals(product + " interest rate", expected[i][1],
                AccountRulesEngine.getInterestRate(product));

            assertEquals(product + " overdraft limit", expected[i][2],
                AccountRulesEngine.getOverdraftLimit(product));
        }


        // =========================================================
        // STEP 2: Multi-product withdrawal validation, driven by a
        // single loop over every known product -- no per-product
        // branching in the test itself, just like the engine.
        
        System.out.println("\n>>> Step 2: Multi-Product Withdrawal Validation");

        assertTrue("SAVINGS: 5000 balance, withdraw 3000 (leaves 2000 >= 1000)",
            AccountRulesEngine.validateWithdrawal("SAVINGS", 5000.0, 3000.0));

        assertFalse("SAVINGS: 5000 balance, withdraw 4500 (leaves 500 < 1000)",
            AccountRulesEngine.validateWithdrawal("SAVINGS", 5000.0, 4500.0));

        assertTrue("CURRENT: 2000 balance, withdraw 11000 (uses overdraft, leaves -9000 >= -10000)",
            AccountRulesEngine.validateWithdrawal("CURRENT", 2000.0, 11000.0));

        assertFalse("CURRENT: 2000 balance, withdraw 13000 (exceeds overdraft)",
            AccountRulesEngine.validateWithdrawal("CURRENT", 2000.0, 13000.0));

        assertTrue("FIXEDDEPOSIT: 10000 balance, withdraw 10000 (leaves exactly 0)",
            AccountRulesEngine.validateWithdrawal("FIXEDDEPOSIT", 10000.0, 10000.0));

        assertFalse("FIXEDDEPOSIT: 10000 balance, withdraw 10000.01 (goes negative)",
            AccountRulesEngine.validateWithdrawal("FIXEDDEPOSIT", 10000.0, 10000.01));

        assertFalse("SALARY: 0 minimum balance and 0 overdraft, withdraw more than balance",
            AccountRulesEngine.validateWithdrawal("SALARY", 100.0, 150.0));


        // =========================================================
        // STEP 3: Naming-style normalization still works after the
        // refactor (dictionary keys are normalized on lookup too).
        
        System.out.println("\n>>> Step 3: Normalization Still Works With Dictionary Lookup");

        assertEquals("lowercase \"savings\" minimum balance", 1000.0,
            AccountRulesEngine.getMinimumBalance("savings"));

        assertEquals("display string \"Fixed Deposit\" interest rate", 6.5,
            AccountRulesEngine.getInterestRate("Fixed Deposit"));


        // =========================================================
        // STEP 4: Adding a brand-new product is a one-line dictionary
        // entry -- no existing method needs to change.
        
        System.out.println("\n>>> Step 4: Registering a New Product at Runtime");

        assertFalse("PLATINUM is not a known product yet",
            AccountRulesEngine.isKnownAccountType("PLATINUM"));

        AccountRulesEngine.registerPolicy("PLATINUM", 25000.0, 5.5, 50000.0);

        assertTrue("PLATINUM is now a known product",
            AccountRulesEngine.isKnownAccountType("PLATINUM"));

        assertEquals("PLATINUM minimum balance", 25000.0,
            AccountRulesEngine.getMinimumBalance("PLATINUM"));

        assertEquals("PLATINUM interest rate", 5.5,
            AccountRulesEngine.getInterestRate("PLATINUM"));

        assertEquals("PLATINUM overdraft limit", 50000.0,
            AccountRulesEngine.getOverdraftLimit("PLATINUM"));

        assertTrue("PLATINUM: 30000 balance, withdraw 4000 (leaves 26000 >= 25000)",
            AccountRulesEngine.validateWithdrawal("PLATINUM", 30000.0, 4000.0));

        assertTrue("PLATINUM: 30000 balance, withdraw 6000 (leaves 24000 < min balance, but within its 50000 overdraft room)",
            AccountRulesEngine.validateWithdrawal("PLATINUM", 30000.0, 6000.0));

        assertFalse("PLATINUM: 30000 balance, withdraw 80000 (exceeds even the overdraft room)",
            AccountRulesEngine.validateWithdrawal("PLATINUM", 30000.0, 80000.0));


        // =========================================================
        // Summary
        
        System.out.println("\n============================================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed");
        System.out.println("============================================================");

        if (failed > 0) {
            throw new AssertionError(failed + " assertion(s) failed");
        }

        System.out.println("Dictionary-backed rules engine verified end-to-end!");
    }


    // ------------------------------------------------------------
    // Assertion helpers
    
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
