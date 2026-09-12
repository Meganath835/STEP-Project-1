package bank;

// ============================================================
// SALARY ACCOUNT
//
// A zero-minimum-balance account tied to an employer, typically
// opened for an employee's monthly salary credit.
// ============================================================

public class SalaryAccount extends Account {

    // ------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------

    private static final double MINIMUM_BALANCE = 0.0;
    private static final String ACCOUNT_TYPE = "Salary";
    private static final double INTEREST_RATE = 3.0;


    // ------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------

    private String employerName;


    // ------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------

    public SalaryAccount(
            int accountNumber,
            String name,
            int age,
            double initialBalance,
            String employerName) {

        super(
            accountNumber,
            name,
            age,
            initialBalance
        );

        this.employerName = employerName;
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
    // SALARY ACCOUNT SPECIFIC METHODS
    // ------------------------------------------------------------

    public String getEmployerName() {
        return employerName;
    }


    public double getInterestRate() {
        return INTEREST_RATE;
    }
}
