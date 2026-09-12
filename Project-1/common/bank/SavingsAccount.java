package bank;

// ============================================================
// SAVINGS ACCOUNT
// ============================================================

public class SavingsAccount extends Account {

    // ------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------

    private static final double MINIMUM_BALANCE = 500.0;
    private static final String ACCOUNT_TYPE = "Savings";
    private static final double INTEREST_RATE = 4.0;


    // ------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------

    public SavingsAccount(
            int accountNumber,
            String name,
            int age,
            double initialBalance) {

        super(
            accountNumber,
            name,
            age,
            initialBalance
        );
    }


    // ------------------------------------------------------------
    // Implement abstract method
    // ------------------------------------------------------------

    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }


    // ------------------------------------------------------------
    // Implement abstract method
    // ------------------------------------------------------------

    @Override
    public String getAccountType() {
        return ACCOUNT_TYPE;
    }


    // ------------------------------------------------------------
    // Implement abstract method
    // Simple Interest = P x R x T
    // ------------------------------------------------------------

    @Override
    public double calculateInterest(int years) {

        if (years < 0) {
            throw new IllegalArgumentException(
                "Years must be non-negative"
            );
        }

        return getBalance()
                * (INTEREST_RATE / 100)
                * years;
    }


    // ------------------------------------------------------------
    // Get Interest Rate
    // ------------------------------------------------------------

    public double getInterestRate() {
        return INTEREST_RATE;
    }
}
