package bank;

// ============================================================
// FIXED DEPOSIT ACCOUNT
// ============================================================

public class FixedDepositAccount extends Account {

    // ------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------

    private static final double MINIMUM_BALANCE = 5000.0;
    private static final String ACCOUNT_TYPE = "Fixed Deposit";
    private static final double INTEREST_RATE = 7.5;


    // ------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------

    private int tenureYears;
    private boolean matured;


    // ------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------

    public FixedDepositAccount(
            int accountNumber,
            String name,
            int age,
            double initialBalance,
            int tenureYears) {

        super(
            accountNumber,
            name,
            age,
            initialBalance
        );

        if (tenureYears <= 0) {
            throw new IllegalArgumentException(
                "Tenure must be at least 1 year"
            );
        }

        this.tenureYears = tenureYears;
        this.matured = false;
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
    // Simple Interest = P x R x T, ignores the years argument
    // since a fixed deposit always matures over its own tenure
    // ------------------------------------------------------------

    @Override
    public double calculateInterest(int years) {
        return getBalance()
                * (INTEREST_RATE / 100)
                * tenureYears;
    }


    // ============================================================
    // OVERRIDE DEPOSIT
    // A fixed deposit cannot receive additional deposits once opened
    // ============================================================

    @Override
    public void deposit(double amount)
            throws InvalidAmountException,
                   InactiveAccountException {

        throw new UnsupportedOperationException(
            "Deposits are not allowed on a Fixed Deposit account"
        );
    }


    // ============================================================
    // OVERRIDE WITHDRAW
    // Withdrawal is only permitted after maturity
    // ============================================================

    @Override
    public void withdraw(double amount, int enteredPin)
            throws InvalidAmountException,
                   InsufficientBalanceException,
                   InactiveAccountException,
                   InvalidPinException,
                   MinimumBalanceViolationException {

        if (!matured) {
            throw new IllegalStateException(
                "Cannot withdraw before maturity. Tenure remaining: "
                + tenureYears
                + " year(s)"
            );
        }

        validateActive();
        validatePin(enteredPin);
        validateAmount(amount);

        if (amount > getBalance()) {
            throw new InsufficientBalanceException(
                "Insufficient balance. Available: ₹"
                + getBalance()
                + ", Requested: ₹"
                + amount
            );
        }

        setBalance(getBalance() - amount);
    }


    // ============================================================
    // FIXED DEPOSIT SPECIFIC METHODS
    // ============================================================

    public int getTenureYears() {
        return tenureYears;
    }


    public boolean isMatured() {
        return matured;
    }


    public void matureDeposit() {

        if (matured) {
            throw new IllegalStateException(
                "Fixed deposit has already matured"
            );
        }

        double maturityInterest = calculateInterest(tenureYears);

        setBalance(getBalance() + maturityInterest);

        matured = true;
    }
}
