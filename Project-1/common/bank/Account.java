package bank;

// ============================================================
// ABSTRACT ACCOUNT CLASS
//
// Shared by every activity that builds on the bank domain model
// (Activity 9, Activity 10, Activity 11, ...). Import it with
// `import bank.*;` instead of copy-pasting the source.
//
// Implements IAccount so client code (AccountFactory callers,
// test drivers) can depend on the interface instead of this
// concrete class.
// ============================================================

public abstract class Account implements IAccount {

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
    // Abstract Methods (contracts every subclass must fulfil)
    // ------------------------------------------------------------

    public abstract double getMinimumBalance();

    public abstract String getAccountType();

    public abstract double calculateInterest(int years);


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
    // TEMPLATE METHOD
    // Uses the abstract hooks getAccountType() and calculateInterest()
    // so every subclass automatically gets a correctly labelled report
    // without overriding this method.
    // ============================================================

    public final void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Balance: ₹" + balance);
        System.out.println("Account Type: " + getAccountType());
        System.out.println("Status: " + status);
        System.out.println(
            "Interest (1 year): ₹" + calculateInterest(1)
        );
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
