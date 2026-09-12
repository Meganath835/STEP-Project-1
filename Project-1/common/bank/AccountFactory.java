package bank;

// ============================================================
// ACCOUNT FACTORY
//
// Decouples client code from concrete account constructors.
// Callers ask for an account by type name and get back an
// IAccount reference -- they never need to know (or import)
// SavingsAccount, CurrentAccount, FixedDepositAccount or
// SalaryAccount directly.
// ============================================================

public class AccountFactory {

    // Prevent instantiation -- every member is static.
    private AccountFactory() {
    }


    // ------------------------------------------------------------
    // Create a Savings, Current or Salary account.
    // ------------------------------------------------------------

    public static IAccount createAccount(
            String accountType,
            int accountNumber,
            String name,
            int age,
            double initialBalance) {

        return createAccount(
            accountType,
            accountNumber,
            name,
            age,
            initialBalance,
            null,
            0
        );
    }


    // ------------------------------------------------------------
    // Create a Salary account (needs an employer name).
    // ------------------------------------------------------------

    public static IAccount createAccount(
            String accountType,
            int accountNumber,
            String name,
            int age,
            double initialBalance,
            String employerName) {

        return createAccount(
            accountType,
            accountNumber,
            name,
            age,
            initialBalance,
            employerName,
            0
        );
    }


    // ------------------------------------------------------------
    // Create a Fixed Deposit account (needs a tenure in years).
    // ------------------------------------------------------------

    public static IAccount createAccount(
            String accountType,
            int accountNumber,
            String name,
            int age,
            double initialBalance,
            int tenureYears) {

        return createAccount(
            accountType,
            accountNumber,
            name,
            age,
            initialBalance,
            null,
            tenureYears
        );
    }


    // ------------------------------------------------------------
    // Single dispatch point every overload funnels through.
    // ------------------------------------------------------------

    private static IAccount createAccount(
            String accountType,
            int accountNumber,
            String name,
            int age,
            double initialBalance,
            String employerName,
            int tenureYears) {

        if (accountType == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }

        switch (accountType.trim().toLowerCase()) {

            case "savings":
                return new SavingsAccount(accountNumber, name, age, initialBalance);

            case "current":
                return new CurrentAccount(accountNumber, name, age, initialBalance);

            case "fixeddeposit":
            case "fixed deposit":
            case "fd":
                return new FixedDepositAccount(
                    accountNumber, name, age, initialBalance, tenureYears
                );

            case "salary":
                return new SalaryAccount(
                    accountNumber, name, age, initialBalance, employerName
                );

            default:
                throw new IllegalArgumentException(
                    "Unknown account type: " + accountType
                );
        }
    }
}
