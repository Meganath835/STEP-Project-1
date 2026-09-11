
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


public class Account {

    // Private fields
    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    private Integer pin;

    // Constructor
    public Account(int accountNumber, String name, int age,
                   double initialBalance, String accountType) {

        // Validate age
        if (age < 18) {
            throw new IllegalArgumentException(
                "Customer must be at least 18 years old. Provided: " + age
            );
        }

        // Validate account type
        if (accountType == null ||
            !(accountType.equals("Savings") || accountType.equals("Current"))) {
            throw new IllegalArgumentException(
                "Account type must be 'Savings' or 'Current'. Provided: " + accountType
            );
        }

        // Validate minimum balance
        double minBalance = accountType.equals("Savings") ? 500.0 : 1000.0;

        if (initialBalance < minBalance) {
            throw new IllegalArgumentException(
                accountType + " account requires minimum balance of ₹"
                + minBalance + ". Provided: ₹" + initialBalance
            );
        }

        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.status = "Active";
        this.pin = null;
    }

    // Returns the minimum balance based on account type
    private double getMinimumBalance() {
        return accountType.equals("Savings") ? 500.0 : 1000.0;
    }

    // Deposit method
    public void deposit(double amount)
            throws InvalidAmountException, InactiveAccountException {

        validateActive();
        validateAmount(amount);

        balance += amount;
    }

    // Withdraw method
    public void withdraw(double amount, int enteredPin)
            throws InvalidAmountException,
                   InsufficientBalanceException,
                   InactiveAccountException,
                   InvalidPinException,
                   MinimumBalanceViolationException {

        validateActive();
        validatePin(enteredPin);
        validateAmount(amount);

        if (amount > balance) {
            throw new InsufficientBalanceException(
                "Insufficient balance. Available: ₹" + balance
                + ", Requested: ₹" + amount
            );
        }

        double newBalance = balance - amount;
        double minimumBalance = getMinimumBalance();

        if (newBalance < minimumBalance) {
            throw new MinimumBalanceViolationException(
                "Cannot withdraw. Minimum balance of ₹" + minimumBalance
                + " required. Available after withdrawal: ₹" + newBalance
            );
        }

        balance = newBalance;
    }

    // Validation helpers
    private void validateActive() throws InactiveAccountException {
        if (!status.equals("Active")) {
            throw new InactiveAccountException(
                "Account is inactive. Please reopen the account or contact support."
            );
        }
    }

    private void validatePin(int enteredPin) throws InvalidPinException {
        if (pin == null) {
            throw new InvalidPinException("PIN not set for this account");
        }

        if (pin != enteredPin) {
            throw new InvalidPinException("Incorrect PIN");
        }
    }

    private void validateAmount(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(
                "Amount must be positive. Provided: ₹" + amount
            );
        }
    }

    // Close account
    public void closeAccount() {
        if (status.equals("Inactive")) {
            throw new IllegalStateException("Account is already closed.");
        }

        status = "Inactive";
    }

    // Reopen account
    public void reopenAccount() {
        if (status.equals("Active")) {
            throw new IllegalStateException("Account is already active.");
        }

        status = "Active";
    }

    // Set PIN
    public void setPin(int newPin) {
        if (newPin < 1000 || newPin > 9999) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits.");
        }

        this.pin = newPin;
    }

    // Verify PIN
    public boolean verifyPin(int enteredPin) {
        if (this.pin == null) {
            return false;
        }

        return this.pin == enteredPin;
    }

    // Check whether PIN is set
    public boolean hasPin() {
        return pin != null;
    }

    // Getter methods
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

    public String getAccountType() {
        return accountType;
    }

    public String getStatus() {
        return status;
    }

    // Setter methods
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
