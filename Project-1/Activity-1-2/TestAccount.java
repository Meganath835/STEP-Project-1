
public class TestAccount {

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("GLOBAL DIGITAL BANK - ACCOUNT TEST");
        System.out.println("==================================================");

        // 1. Creating Account
        System.out.println(">>> 1. Creating Account");

        Account account1 = new Account(
                1001,
                "John Doe",
                25,
                1000.0,
                "Savings"
        );

        System.out.println("Account created!");
        displayAccount(account1);

        // 2. Deposit Money
        System.out.println(">>> 2. Deposit Money");

        double depositAmount = 500.0;
        boolean depositResult = account1.deposit(depositAmount);

        if (depositResult) {
            System.out.println("Depositing ₹" + depositAmount + ": SUCCESS");
            System.out.println("New balance: ₹" + account1.getBalance());
        } else {
            System.out.println("Depositing ₹" + depositAmount + ": FAILED (Invalid amount)");
        }

        // Invalid deposit
        depositAmount = -100.0;
        depositResult = account1.deposit(depositAmount);

        if (depositResult) {
            System.out.println("Depositing ₹" + depositAmount + ": SUCCESS");
        } else {
            System.out.println("Depositing ₹" + depositAmount + ": FAILED (Invalid amount)");
        }

        // 3. Withdraw Money
        System.out.println(">>> 3. Withdraw Money");

        double withdrawAmount = 200.0;
        boolean withdrawResult = account1.withdraw(withdrawAmount);

        if (withdrawResult) {
            System.out.println("Withdrawing ₹" + withdrawAmount + ": SUCCESS");
            System.out.println("New balance: ₹" + account1.getBalance());
        } else {
            System.out.println("Withdrawing ₹" + withdrawAmount + ": FAILED (Insufficient balance)");
        }

        // Insufficient balance
        withdrawAmount = 2000.0;
        withdrawResult = account1.withdraw(withdrawAmount);

        if (withdrawResult) {
            System.out.println("Withdrawing ₹" + withdrawAmount + ": SUCCESS");
        } else {
            System.out.println("Withdrawing ₹" + withdrawAmount + ": FAILED (Insufficient balance)");
        }

        System.out.println("Current balance: ₹" + account1.getBalance());

        // 4. Creating Another Account
        System.out.println(">>> 4. Creating Another Account");

        Account account2 = new Account(
                1002,
                "Jane Smith",
                30,
                2000.0,
                "Current"
        );

        displayAccount(account2);

        // 5. All Accounts
        System.out.println(">>> 5. All Accounts");

        displayAccount(account1);
        displayAccount(account2);

        System.out.println("==================================================");
        System.out.println("TEST COMPLETED!");
        System.out.println("==================================================");
    }

    // Display method belongs to the test class, NOT Account
    public static void displayAccount(Account account) {

        System.out.println(
                "Account #" + account.getAccountNumber()
                + " | " + account.getName()
                + " (" + account.getAge() + " yrs)"
                + " | " + account.getAccountType()
                + " | ₹" + account.getBalance()
                + " | " + account.getStatus()
        );
    }
}
