
public class AccountEnhanced {

    // Private fields
    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    private Integer pin;

    // Constructor
    public AccountEnhanced(int accountNumber, String name, int age,
                           double initialBalance, String accountType) {

        this.accountNumber = accountNumber;
        this.name = name;

        // Age validation
        if (age < 18) {
            this.age = 18;
        } else {
            this.age = age;
        }

        // Account type validation
        if (accountType != null &&
            (accountType.equals("Savings") || accountType.equals("Current"))) {
            this.accountType = accountType;
        } else {
            this.accountType = "Savings";
        }

        // Set minimum balance
        double minimumBalance = getMinimumBalance();

        if (initialBalance < minimumBalance) {
            this.balance = minimumBalance;
        } else {
            this.balance = initialBalance;
        }

        // Account is Active by default
        this.status = "Active";

        // PIN is not set initially
        this.pin = null;
    }

    // Returns the minimum balance based on account type
    private double getMinimumBalance() {

        if (accountType.equals("Savings")) {
            return 500.0;
        } else {
            return 1000.0;
        }
    }

    // Deposit money
    public boolean deposit(double amount) {

        // Account must be active
        if (!status.equals("Active")) {
            return false;
        }

        // Amount must be positive
        if (amount <= 0) {
            return false;
        }

        balance += amount;
        return true;
    }

    // Withdraw money with PIN
    public boolean withdraw(double amount, int pin) {

        // Account must be active
        if (!status.equals("Active")) {
            return false;
        }

        // PIN must be correct
        if (!verifyPin(pin)) {
            return false;
        }

        // Amount must be positive
        if (amount <= 0) {
            return false;
        }

        // Balance must not fall below minimum balance
        double minimumBalance = getMinimumBalance();

        if (balance - amount < minimumBalance) {
            return false;
        }

        balance -= amount;
        return true;
    }

    // Close account
    public boolean closeAccount() {

        if (status.equals("Inactive")) {
            return false;
        }

        status = "Inactive";
        return true;
    }

    // Reopen account
    public boolean reopenAccount() {

        if (status.equals("Active")) {
            return false;
        }

        status = "Active";
        return true;
    }

    // Set PIN
    public boolean setPin(int pin) {

        // PIN must be exactly 4 digits
        if (pin < 1000 || pin > 9999) {
            return false;
        }

        this.pin = pin;
        return true;
    }

    // Verify PIN
    public boolean verifyPin(int pin) {

        // If no PIN has been set
        if (this.pin == null) {
            return false;
        }

        return this.pin == pin;
    }

    // Check whether PIN is set
    public boolean hasPin() {
        return pin != null;
    }

    // Getters

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

    // Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
