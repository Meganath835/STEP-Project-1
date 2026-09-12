package bank;

// ============================================================
// CURRENT ACCOUNT
// ============================================================

public class CurrentAccount extends Account {

    // ------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------

    private static final double MINIMUM_BALANCE = 1000.0;
    private static final String ACCOUNT_TYPE = "Current";
    private static final double OVERDRAFT_LIMIT = 5000.0;


    // ------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------

    private double overdraftUsed;


    // ------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------

    public CurrentAccount(
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

        overdraftUsed = 0.0;
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
    // Current accounts do not earn interest
    // ------------------------------------------------------------

    @Override
    public double calculateInterest(int years) {
        return 0.0;
    }


    // ============================================================
    // OVERRIDE WITHDRAW
    // ============================================================

    @Override
    public void withdraw(double amount, int enteredPin)
            throws InvalidAmountException,
                   InsufficientBalanceException,
                   InactiveAccountException,
                   InvalidPinException,
                   MinimumBalanceViolationException {

        // Common validation
        validateActive();
        validatePin(enteredPin);
        validateAmount(amount);


        // --------------------------------------------------------
        // Calculate total amount available
        //
        // Example:
        // Balance = Rs 1500
        // Minimum = Rs 1000
        // Overdraft = Rs 5000
        //
        // Available = 1500 - 1000 + 5000
        //           = Rs 5500
        // --------------------------------------------------------

        double availableBalance =
            getBalance()
            - getMinimumBalance()
            + OVERDRAFT_LIMIT
            - overdraftUsed;


        // --------------------------------------------------------
        // Check whether withdrawal is possible
        // --------------------------------------------------------

        if (amount > availableBalance) {

            throw new InsufficientBalanceException(
                "Insufficient funds. Available: ₹"
                + availableBalance
                + " (including ₹"
                + OVERDRAFT_LIMIT
                + " overdraft), Requested: ₹"
                + amount
            );
        }


        // --------------------------------------------------------
        // Calculate new balance
        // --------------------------------------------------------

        double newBalance = getBalance() - amount;


        // --------------------------------------------------------
        // If balance goes below minimum,
        // use overdraft facility
        // --------------------------------------------------------

        if (newBalance < getMinimumBalance()) {

            double overdraftAmount =
                getMinimumBalance() - newBalance;

            overdraftUsed += overdraftAmount;
        }


        // Update balance
        setBalance(newBalance);
    }


    // ============================================================
    // CURRENT ACCOUNT SPECIFIC METHODS
    // ============================================================

    public double getOverdraftLimit() {
        return OVERDRAFT_LIMIT;
    }


    public double getOverdraftUsed() {
        return overdraftUsed;
    }


    public double getAvailableOverdraft() {
        return OVERDRAFT_LIMIT - overdraftUsed;
    }


    public boolean isUsingOverdraft() {
        return overdraftUsed > 0;
    }


    // ============================================================
    // REPAY OVERDRAFT
    // ============================================================

    public void repayOverdraft(double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Repayment amount must be positive"
            );
        }


        if (amount > overdraftUsed) {
            throw new IllegalArgumentException(
                "Amount exceeds overdraft used (₹"
                + overdraftUsed
                + ")"
            );
        }


        overdraftUsed -= amount;

        setBalance(getBalance() + amount);
    }
}
