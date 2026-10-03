package bank;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

// ============================================================
// ACCOUNT RULES ENGINE
//
// Centralizes policy thresholds (minimum balance, interest rate,
// overdraft limit) that previously would have been scattered as
// magic numbers across the individual account subclasses.
//
// Activity 13.2: rules are stored in a dictionary (Map) keyed by
// account type, giving O(1) lookup instead of the if/else chain
// from Activity 13.1 -- adding a new product is now a matter of
// adding one entry to POLICIES rather than editing every method.
//
// Activity 14: the dictionary is no longer hard-coded in Java --
// it is populated from the external .properties files under
// bank/config/rules/ via AccountRulesPropertiesLoader. Editing
// one of those files and calling reloadPolicies() changes policy
// thresholds without recompiling or restarting the application.
// ============================================================

public class AccountRulesEngine {

    // ------------------------------------------------------------
    // Policy record -- the three thresholds for one product type.
    // ------------------------------------------------------------

    private static final class Policy {
        final double minimumBalance;
        final double interestRate;
        final double overdraftLimit;

        Policy(double minimumBalance, double interestRate, double overdraftLimit) {
            this.minimumBalance = minimumBalance;
            this.interestRate = interestRate;
            this.overdraftLimit = overdraftLimit;
        }
    }


    // ------------------------------------------------------------
    // Every account type whose policy is externalized to a
    // bank/config/rules/<type>.properties file.
    // ------------------------------------------------------------

    private static final String[] EXTERNALLY_CONFIGURED_TYPES = {
        "SAVINGS", "CURRENT", "FIXEDDEPOSIT", "SALARY"
    };


    // ------------------------------------------------------------
    // Dictionary of policies, keyed by normalized account type.
    // O(1) lookup for every rule, and the single place to add or
    // change a product's policy.
    // ------------------------------------------------------------

    private static final Policy DEFAULT_POLICY = new Policy(0.0, 0.0, 0.0);

    private static final Map<String, Policy> POLICIES = new HashMap<>();

    static {
        reloadPolicies();
    }


    // ------------------------------------------------------------
    // Re-reads every externally configured account type's
    // .properties file and rebuilds the dictionary. Call this
    // after editing a .properties file to pick up the change in
    // the running application -- no recompiling, no restarting.
    // ------------------------------------------------------------

    public static void reloadPolicies() {

        for (String type : EXTERNALLY_CONFIGURED_TYPES) {

            Properties properties = AccountRulesPropertiesLoader.loadProperties(type);

            double minimumBalance = AccountRulesPropertiesLoader.getDouble(
                properties, "minBalance", 0.0
            );
            double interestRate = AccountRulesPropertiesLoader.getDouble(
                properties, "interestRate", 0.0
            );
            double overdraftLimit = AccountRulesPropertiesLoader.getDouble(
                properties, "overdraftLimit", 0.0
            );

            POLICIES.put(type, new Policy(minimumBalance, interestRate, overdraftLimit));
        }
    }


    // Prevent instantiation -- every member is static.
    private AccountRulesEngine() {
    }


    // ------------------------------------------------------------
    // Normalizes an account type so callers can pass either the
    // factory's lowercase keys ("savings"), the display strings
    // returned by getAccountType() ("Fixed Deposit"), or the
    // upper-case policy keys ("FIXEDDEPOSIT") interchangeably.
    // ------------------------------------------------------------

    private static String normalize(String accountType) {

        if (accountType == null) {
            return "";
        }

        return accountType.trim().replace(" ", "").toUpperCase();
    }


    private static Policy policyFor(String accountType) {
        return POLICIES.getOrDefault(normalize(accountType), DEFAULT_POLICY);
    }


    // ============================================================
    // MINIMUM BALANCE RULE -- O(1) dictionary lookup
    // ============================================================

    public static double getMinimumBalance(String accountType) {
        return policyFor(accountType).minimumBalance;
    }


    // ============================================================
    // INTEREST RATE & OVERDRAFT RULES -- O(1) dictionary lookup
    // ============================================================

    public static double getInterestRate(String accountType) {
        return policyFor(accountType).interestRate;
    }


    public static double getOverdraftLimit(String accountType) {
        return policyFor(accountType).overdraftLimit;
    }


    // ============================================================
    // WITHDRAWAL RULE VALIDATION
    //
    // Combines the minimum balance and overdraft limit rules: a
    // withdrawal is allowed as long as the resulting balance does
    // not fall below (minimum balance - overdraft limit) -- i.e.
    // the overdraft limit is "spare room" under the minimum.
    // ============================================================

    public static boolean validateWithdrawal(
            String accountType,
            double currentBalance,
            double withdrawalAmount) {

        if (withdrawalAmount <= 0) {
            return false;
        }

        Policy policy = policyFor(accountType);

        double balanceAfterWithdrawal = currentBalance - withdrawalAmount;
        double floor = policy.minimumBalance - policy.overdraftLimit;

        return balanceAfterWithdrawal >= floor;
    }


    // ============================================================
    // Registers or overrides a product's policy at runtime --
    // demonstrates that adding a new account type to the engine
    // no longer requires touching any if/else logic.
    // ============================================================

    public static void registerPolicy(
            String accountType,
            double minimumBalance,
            double interestRate,
            double overdraftLimit) {

        POLICIES.put(normalize(accountType), new Policy(minimumBalance, interestRate, overdraftLimit));
    }


    public static boolean isKnownAccountType(String accountType) {
        return POLICIES.containsKey(normalize(accountType));
    }
}
