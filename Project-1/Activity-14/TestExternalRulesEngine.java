
// ============================================================
// ACTIVITY 14: EXTERNAL PROPERTIES RULES ENGINE
//

import bank.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class TestExternalRulesEngine {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws IOException {

        System.out.println("============================================================");
        System.out.println("   ACTIVITY 14: EXTERNAL PROPERTIES RULES ENGINE TEST");
        System.out.println("============================================================");

        // =========================================================
        // STEP 1: Values in the running engine match what is
        // written in the .properties files (not Java literals).
        
        System.out.println("\n>>> Step 1: Policies Loaded From External .properties Files");

        assertEquals("SAVINGS minBalance (savings.properties)", 1000.0,
            AccountRulesEngine.getMinimumBalance("SAVINGS"));
        assertEquals("SAVINGS interestRate (savings.properties)", 4.0,
            AccountRulesEngine.getInterestRate("SAVINGS"));

        assertEquals("CURRENT overdraftLimit (current.properties)", 10000.0,
            AccountRulesEngine.getOverdraftLimit("CURRENT"));
        assertEquals("CURRENT minBalance (current.properties)", 0.0,
            AccountRulesEngine.getMinimumBalance("CURRENT"));

        assertEquals("FIXEDDEPOSIT interestRate (fixeddeposit.properties)", 6.5,
            AccountRulesEngine.getInterestRate("FIXEDDEPOSIT"));

        assertEquals("SALARY minBalance (salary.properties)", 0.0,
            AccountRulesEngine.getMinimumBalance("SALARY"));
        assertEquals("SALARY overdraftLimit (salary.properties)", 0.0,
            AccountRulesEngine.getOverdraftLimit("SALARY"));


        // =========================================================
        // STEP 2: The loader itself parses key=value lines into a
        // dictionary (java.util.Properties) directly.
        // =========================================================
        System.out.println("\n>>> Step 2: AccountRulesPropertiesLoader Parses key=value Lines");

        java.util.Properties savingsProps = AccountRulesPropertiesLoader.loadProperties("savings");
        assertEquals("savings.properties key \"minBalance\" parsed", 1000.0,
            AccountRulesPropertiesLoader.getDouble(savingsProps, "minBalance", -1.0));
        assertEquals("savings.properties key \"interestRate\" parsed", 4.0,
            AccountRulesPropertiesLoader.getDouble(savingsProps, "interestRate", -1.0));

        assertEquals("Missing key falls back to caller's default", -1.0,
            AccountRulesPropertiesLoader.getDouble(savingsProps, "overdraftLimit", -1.0));


        // =========================================================
        // STEP 3: Dynamic reload -- edit fixeddeposit.properties on
        // disk, then call reloadPolicies() and see the new value
        // take effect immediately, in this same running program.
        // =========================================================
        System.out.println("\n>>> Step 3: Dynamic Reload Without Restarting");

        File fdFile = AccountRulesPropertiesLoader.resolvePropertiesFile("fixeddeposit");

        if (fdFile == null || !fdFile.exists()) {
            fail("Could not resolve fixeddeposit.properties on disk");
        } else {

            String originalContent = new String(Files.readAllBytes(fdFile.toPath()));

            try {
                System.out.println("Before edit: FIXEDDEPOSIT interestRate = "
                    + AccountRulesEngine.getInterestRate("FIXEDDEPOSIT"));

                assertEquals("FIXEDDEPOSIT interestRate before edit", 6.5,
                    AccountRulesEngine.getInterestRate("FIXEDDEPOSIT"));

                // Simulate an operator editing the policy file live.
                try (FileWriter writer = new FileWriter(fdFile)) {
                    writer.write("# Fixed Deposit account policy thresholds (temporarily bumped)\n");
                    writer.write("minBalance=0.0\n");
                    writer.write("interestRate=8.25\n");
                }

                // Without this call the engine would still be
                // serving the old cached value.
                AccountRulesEngine.reloadPolicies();

                System.out.println("After edit + reloadPolicies(): FIXEDDEPOSIT interestRate = "
                    + AccountRulesEngine.getInterestRate("FIXEDDEPOSIT"));

                assertEquals("FIXEDDEPOSIT interestRate after edit + reload", 8.25,
                    AccountRulesEngine.getInterestRate("FIXEDDEPOSIT"));

                // Other products are unaffected by the edit.
                assertEquals("SAVINGS interestRate unaffected by FD edit", 4.0,
                    AccountRulesEngine.getInterestRate("SAVINGS"));

            } finally {
                // Restore the file so the repository stays clean
                // and subsequent test runs see the original policy.
                try (FileWriter writer = new FileWriter(fdFile)) {
                    writer.write(originalContent);
                }
                AccountRulesEngine.reloadPolicies();

                assertEquals("FIXEDDEPOSIT interestRate restored", 6.5,
                    AccountRulesEngine.getInterestRate("FIXEDDEPOSIT"));
            }
        }


        // =========================================================
        // STEP 4: Withdrawal validation still combines the
        // externally configured minimum balance and overdraft limit
        // correctly.
        // =========================================================
        System.out.println("\n>>> Step 4: Withdrawal Validation Using External Policies");

        assertTrue("CURRENT: 2000 balance, withdraw 11000 (within 10000 overdraft room)",
            AccountRulesEngine.validateWithdrawal("CURRENT", 2000.0, 11000.0));

        assertFalse("SAVINGS: 1500 balance, withdraw 700 (would leave 800 < 1000 minimum)",
            AccountRulesEngine.validateWithdrawal("SAVINGS", 1500.0, 700.0));


        // =========================================================
        // Summary
        // =========================================================
        System.out.println("\n============================================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed");
        System.out.println("============================================================");

        if (failed > 0) {
            throw new AssertionError(failed + " assertion(s) failed");
        }

        System.out.println("External properties rules engine verified successfully!");
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

    private static void assertTrue(String label, boolean actual) {
        if (actual) {
            pass(label + " -> allowed");
        } else {
            fail(label + " expected allowed but was rejected");
        }
    }

    private static void assertFalse(String label, boolean actual) {
        if (!actual) {
            pass(label + " -> rejected");
        } else {
            fail(label + " expected rejected but was allowed");
        }
    }
}
