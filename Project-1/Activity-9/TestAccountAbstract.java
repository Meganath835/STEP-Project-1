
import bank.*;

public class TestAccountAbstract {

    public static void main(String[] args) {

        System.out.println("============================================================");
        System.out.println("   ACTIVITY 9: ABSTRACT CLASSES AND TEMPLATE METHODS TEST");
        System.out.println("============================================================");

        // =========================================================
        // TEST 1: Creating Accounts
        // =========================================================
        System.out.println("\n>>> Test 1: Creating Accounts");

        SavingsAccount savings = new SavingsAccount(1001, "John Doe", 25, 1000);
        CurrentAccount current = new CurrentAccount(1002, "Jane Smith", 30, 2000);
        FixedDepositAccount fd = new FixedDepositAccount(1003, "Ravi Kumar", 40, 10000, 3);

        System.out.println("Created: " + savings.getAccountType() + " account");
        System.out.println("Created: " + current.getAccountType() + " account");
        System.out.println("Created: " + fd.getAccountType() + " account (tenure "
            + fd.getTenureYears() + " years)");


        // =========================================================
        // TEST 2: Template Method - displayAccountInfo()
        // Each subclass reuses the same template method, but the
        // account type and interest are supplied by its own
        // implementation of the abstract hooks.
        // =========================================================
        System.out.println("\n>>> Test 2: Template Method - displayAccountInfo()");

        System.out.println("--- Savings ---");
        savings.displayAccountInfo();

        System.out.println("--- Current ---");
        current.displayAccountInfo();

        System.out.println("--- Fixed Deposit ---");
        fd.displayAccountInfo();


        // =========================================================
        // TEST 3: Polymorphism across all account types
        // =========================================================
        System.out.println("\n>>> Test 3: Polymorphism - Treating Accounts Uniformly");

        Account[] accounts = { savings, current, fd };

        for (Account account : accounts) {
            System.out.println(
                account.getAccountType()
                + " | Balance: ₹" + account.getBalance()
                + " | Interest (1yr): ₹" + account.calculateInterest(1)
            );
        }


        // =========================================================
        // TEST 4: Fixed Deposit specific behaviour
        // =========================================================
        System.out.println("\n>>> Test 4: Fixed Deposit Specific Behaviour");

        System.out.println("Attempting to deposit into a Fixed Deposit account");
        try {
            fd.deposit(500);
        } catch (UnsupportedOperationException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        System.out.println("Attempting to withdraw before maturity");
        try {
            fd.withdraw(100, 0);
        } catch (IllegalStateException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        System.out.println("Balance before maturity: ₹" + fd.getBalance());
        fd.matureDeposit();
        System.out.println("Balance after maturity: ₹" + fd.getBalance());

        fd.setPin(1234);
        try {
            fd.withdraw(1000, 1234);
            System.out.println("Withdrawing ₹1000.0 after maturity: SUCCESS");
            System.out.println("Balance after withdrawal: ₹" + fd.getBalance());
        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }


        // =========================================================
        // TEST 5: Abstract class cannot be instantiated
        // (verified at compile time - Account is declared abstract,
        // so `new Account(...)` below would fail to compile if uncommented)
        // =========================================================
        System.out.println("\n>>> Test 5: Abstract Class Contract");
        System.out.println(
            "Account is declared 'abstract' - it cannot be instantiated directly."
        );
        System.out.println(
            "Every subclass must implement getMinimumBalance(), getAccountType() "
            + "and calculateInterest()."
        );


        // =========================================================
        // COMPLETED
        // =========================================================
        System.out.println("\n============================================================");
        System.out.println("                  TEST COMPLETED!");
        System.out.println("============================================================");
    }
}
