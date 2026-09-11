

class AccountException extends Exception {
    public AccountException(String message) {
        super(message);
    }
}

class InvalidAmountException extends AccountException {
    public InvalidAmountException(String message) {
        super(message);
    }
}

class InsufficientBalanceException extends AccountException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

class InactiveAccountException extends AccountException {
    public InactiveAccountException(String message) {
        super(message);
    }
}

class InvalidPinException extends AccountException {
    public InvalidPinException(String message) {
        super(message);
    }
}

class MinimumBalanceViolationException extends AccountException {
    public MinimumBalanceViolationException(String message) {
        super(message);
    }
}


// ============================================================
// ABSTRACT ACCOUNT CLASS
// ============================================================

public abstract class Account {

    // ------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------

    protected static final int MIN_AGE = 18;
    protected static final int MIN_PIN = 1000;
    protected static final int MAX_PIN = 9999;


    // ------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------

    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String status;
    private Integer pin;


    // ------------------------------------------------------------
    // Abstract Methods
    // ------------------------------------------------------------

    public abstract double getMinimumBalance();

    public abstract String getAccountType();


    // ------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------

    public Account(
            int accountNumber,
            String name,
            int age,
            double initialBalance) {

        // Validate age
        if (age < MIN_AGE) {
            throw new IllegalArgumentException(
                "Customer must be at least "
                + MIN_AGE
                + " years old. Provided: "
                + age
            );
        }

        // Validate minimum balance
        double minBalance = getMinimumBalance();

        if (initialBalance < minBalance) {
            throw new IllegalArgumentException(
                getAccountType()
                + " account requires minimum balance of ₹"
                + minBalance
                + ". Provided: ₹"
                + initialBalance
            );
        }

        // Initialize fields
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = initialBalance;
        this.status = "Active";
        this.pin = null;
    }


    // ============================================================
    // DEPOSIT
    // ============================================================

    public void deposit(double amount)
            throws InvalidAmountException,
                   InactiveAccountException {

        validateActive();
        validateAmount(amount);

        balance += amount;
    }


    // ============================================================
    // WITHDRAW
    // ============================================================

    public void withdraw(double amount, int enteredPin)
            throws InvalidAmountException,
                   InsufficientBalanceException,
                   InactiveAccountException,
                   InvalidPinException,
                   MinimumBalanceViolationException {

        validateActive();
        validatePin(enteredPin);
        validateAmount(amount);

        // Check sufficient balance
        if (amount > balance) {
            throw new InsufficientBalanceException(
                "Insufficient balance. Available: ₹"
                + balance
                + ", Requested: ₹"
                + amount
            );
        }

        // Check minimum balance
        double newBalance = balance - amount;

        if (newBalance < getMinimumBalance()) {
            throw new MinimumBalanceViolationException(
                "Cannot withdraw. Minimum balance of ₹"
                + getMinimumBalance()
                + " required. Available after withdrawal: ₹"
                + newBalance
            );
        }

        balance = newBalance;
    }


    // ============================================================
    // VALIDATION METHODS
    // ============================================================

    protected void validateActive()
            throws InactiveAccountException {

        if (!status.equals("Active")) {
            throw new InactiveAccountException(
                "Account is inactive. Please reopen the account "
                + "or contact support."
            );
        }
    }


    protected void validatePin(int enteredPin)
            throws InvalidPinException {

        if (pin == null) {
            throw new InvalidPinException(
                "PIN not set for this account"
            );
        }

        if (pin != enteredPin) {
            throw new InvalidPinException(
                "Incorrect PIN"
            );
        }
    }


    protected void validateAmount(double amount)
            throws InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException(
                "Amount must be positive. Provided: ₹"
                + amount
            );
        }
    }


    // ============================================================
    // ACCOUNT STATUS
    // ============================================================

    public void closeAccount() {

        if (status.equals("Inactive")) {
            throw new IllegalStateException(
                "Account is already closed."
            );
        }

        status = "Inactive";
    }


    public void reopenAccount() {

        if (status.equals("Active")) {
            throw new IllegalStateException(
                "Account is already active."
            );
        }

        status = "Active";
    }


    // ============================================================
    // PIN METHODS
    // ============================================================

    public void setPin(int newPin) {

        if (newPin < MIN_PIN || newPin > MAX_PIN) {
            throw new IllegalArgumentException(
                "PIN must be exactly 4 digits."
            );
        }

        pin = newPin;
    }


    public boolean verifyPin(int enteredPin) {

        if (pin == null) {
            return false;
        }

        return pin == enteredPin;
    }


    public boolean hasPin() {
        return pin != null;
    }


    // ============================================================
    // PROTECTED BALANCE SETTER
    // Used by CurrentAccount
    // ============================================================

    protected void setBalance(double balance) {
        this.balance = balance;
    }


    // ============================================================
    // GETTERS
    // ============================================================

    public int getAccountNumber() {
        return accountNumber;
    }


    public String getName() {
        return name;
    }


    public int getAge() {
        return age;
    }


    public double getBalance() {
        return balance;
    }


    public String getStatus() {
        return status;
    }


    public Integer getPin() {
        return pin;
    }


    // ============================================================
    // SETTERS
    // ============================================================

    public void setName(String name) {
        this.name = name;
    }


    public void setAge(int age) {
        this.age = age;
    }
}


// ============================================================
// SAVINGS ACCOUNT
// ============================================================

class SavingsAccount extends Account {

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
    // Calculate Interest
    // Simple Interest = P × R × T
    // ------------------------------------------------------------

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


// ============================================================
// CURRENT ACCOUNT
// ============================================================

class CurrentAccount extends Account {

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
        // Balance = ₹1500
        // Minimum = ₹1000
        // Overdraft = ₹5000
        //
        // Available = 1500 - 1000 + 5000
        //           = ₹5500
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


