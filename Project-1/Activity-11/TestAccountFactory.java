
// ============================================================
// ACTIVITY 11: IACCOUNT INTERFACE AND FACTORY PATTERN
//
// Demonstrates decoupling client code from concrete account
// constructors: every account here is obtained from
// AccountFactory and held only as an IAccount reference --
// this file never names SavingsAccount, CurrentAccount,
// FixedDepositAccount or SalaryAccount directly.
//
// The domain classes (IAccount, Account, SavingsAccount,
// CurrentAccount, FixedDepositAccount, SalaryAccount,
// AccountFactory) live once in ../common/bank and are imported
// here rather than copy-pasted.
// ============================================================

import bank.*;

public class TestAccountFactory {

    public static void main(String[] args) {

        System.out.println("============================================================");
        System.out.println("   ACTIVITY 11: IACCOUNT INTERFACE AND FACTORY PATTERN TEST");
        System.out.println("============================================================");

        // =========================================================
        // TEST 1: Creating accounts through the factory
        // Client code depends only on IAccount + AccountFactory.
        // =========================================================
        System.out.println("\n>>> Test 1: Creating Accounts via AccountFactory");

        IAccount savings = AccountFactory.createAccount(
            "savings", 1101, "John Doe", 25, 1000.0
        );

        IAccount current = AccountFactory.createAccount(
            "current", 1102, "Jane Smith", 30, 2000.0
        );

        IAccount fd = AccountFactory.createAccount(
            "fixeddeposit", 1103, "Ravi Kumar", 40, 10000.0, 2
        );

        IAccount salary = AccountFactory.createAccount(
            "salary", 1104, "Priya Nair", 28, 0.0, "Acme Corp"
        );

        System.out.println("Created: " + savings.getAccountType());
        System.out.println("Created: " + current.getAccountType());
        System.out.println("Created: " + fd.getAccountType());
        System.out.println("Created: " + salary.getAccountType());


        // =========================================================
        // TEST 2: Polymorphism through the IAccount interface
        // =========================================================
        System.out.println("\n>>> Test 2: Polymorphism via IAccount");

        IAccount[] accounts = { savings, current, fd, salary };

        for (IAccount account : accounts) {
            account.displayAccountInfo();
            System.out.println("---");
        }


        // =========================================================
        // TEST 3: Interface-driven interest calculation
        // =========================================================
        System.out.println("\n>>> Test 3: Interest Calculation (via IAccount)");

        for (IAccount account : accounts) {
            System.out.println(
                account.getAccountType()
                + " | Min Balance: ₹" + account.getMinimumBalance()
                + " | Interest (1yr): ₹" + account.calculateInterest(1)
            );
        }


        // =========================================================
        // TEST 4: Unknown account type is rejected by the factory
        // =========================================================
        System.out.println("\n>>> Test 4: Unknown Account Type");

        try {
            AccountFactory.createAccount("crypto", 1105, "Nobody", 30, 100.0);
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }


        // =========================================================
        // TEST 5: Deposit/withdraw through the interface only
        // =========================================================
        System.out.println("\n>>> Test 5: Deposit/Withdraw via IAccount");

        try {
            savings.deposit(500);
            System.out.println("Deposited ₹500 into savings account");
            System.out.println("Balance: ₹" + savings.getBalance());

            savings.setPin(1234);
            savings.withdraw(200, 1234);
            System.out.println("Withdrew ₹200 from savings account");
            System.out.println("Balance: ₹" + savings.getBalance());

        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }


        // =========================================================
        // COMPLETED
        // =========================================================
        System.out.println("\n============================================================");
        System.out.println("                  TEST COMPLETED!");
        System.out.println("============================================================");
    }
}
