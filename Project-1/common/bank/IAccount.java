package bank;

// ============================================================
// IACCOUNT INTERFACE
//
// The contract every bank account must fulfil. Client code
// (AccountFactory callers, test drivers, ...) should depend on
// this interface rather than on any concrete account class, so
// new account types can be introduced without touching callers.
// ============================================================

public interface IAccount {

    // ------------------------------------------------------------
    // Core operations
    // ------------------------------------------------------------

    void deposit(double amount)
            throws InvalidAmountException,
                   InactiveAccountException;

    void withdraw(double amount, int enteredPin)
            throws InvalidAmountException,
                   InsufficientBalanceException,
                   InactiveAccountException,
                   InvalidPinException,
                   MinimumBalanceViolationException;

    void displayAccountInfo();


    // ------------------------------------------------------------
    // Abstract contracts (template-method hooks)
    // ------------------------------------------------------------

    double calculateInterest(int years);

    String getAccountType();

    double getMinimumBalance();


    // ------------------------------------------------------------
    // Account lifecycle
    // ------------------------------------------------------------

    void closeAccount();

    void reopenAccount();

    void setPin(int newPin);

    boolean verifyPin(int enteredPin);

    boolean hasPin();


    // ------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------

    int getAccountNumber();

    String getName();

    int getAge();

    double getBalance();

    String getStatus();
}
