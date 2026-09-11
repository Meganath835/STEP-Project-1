
public class TestAccountEnhanced {

    // Display account information
    public static void displayAccount(AccountEnhanced account) {

        String pinStatus;

        if (account.hasPin()) {
            pinStatus = "Yes";
        } else {
            pinStatus = "No";
        }

        System.out.println(
                "Account #" + account.getAccountNumber()
                + " | " + account.getName()
                + " (" + account.getAge() + " yrs)"
                + " | " + account.getAccountType()
                + " | ₹" + account.getBalance()
                + " | " + account.getStatus()
                + " | PIN: " + pinStatus
        );
    }

    public static void main(String[] args) {

        System.out.println("============================================================");
        System.out.println("ENHANCED ACCOUNT TEST (BOOLEAN RETURNS)");
        System.out.println("============================================================");
        System.out.println();

       
        // Test 1: Valid Account Creation
        

        System.out.println(">>> Test 1: Valid Account Creation");

        AccountEnhanced account1 = new AccountEnhanced(
                1001,
                "John Doe",
                25,
                1000.0,
                "Savings"
        );

        displayAccount(account1);
        System.out.println();

        
        // Test 2: Invalid Age
        

        System.out.println(">>> Test 2: Invalid Age (under 18)");
        System.out.println("Creating account with age 16");

        AccountEnhanced account2 = new AccountEnhanced(
                1002,
                "Young Kid",
                16,
                500.0,
                "Savings"
        );

        System.out.println(
                "Age auto-corrected to: " + account2.getAge()
        );

        displayAccount(account2);
        System.out.println();

        
        // Test 3: Invalid Account Type
       

        System.out.println(">>> Test 3: Invalid Account Type");
        System.out.println("Creating account with type \"Invalid\"");

        AccountEnhanced account3 = new AccountEnhanced(
                1003,
                "Test User",
                25,
                500.0,
                "Invalid"
        );

        System.out.println(
                "Account type defaulted to: "
                + account3.getAccountType()
        );

        displayAccount(account3);
        System.out.println();

        
        // Test 4: Minimum Balance on Creation
        

        System.out.println(">>> Test 4: Minimum Balance Enforcement on Creation");
        System.out.println("Creating Savings account with ₹300 (below minimum)");

        AccountEnhanced account4 = new AccountEnhanced(
                1004,
                "Bob Wilson",
                25,
                300.0,
                "Savings"
        );

        System.out.println(
                "Balance auto-corrected to minimum: ₹"
                + account4.getBalance()
        );

        displayAccount(account4);
        System.out.println();

        
        // Test 5: Minimum Balance on Withdrawal
        

        System.out.println(">>> Test 5: Withdrawal with Minimum Balance");

        AccountEnhanced account5 = new AccountEnhanced(
                1005,
                "Alice Brown",
                30,
                1500.0,
                "Current"
        );

        account5.setPin(1234);

        System.out.print("Initial: ");
        displayAccount(account5);

        // Valid withdrawal: 1500 - 200 = 1300
        double withdrawalAmount = 200.0;

        boolean result = account5.withdraw(
                withdrawalAmount,
                1234
        );

        if (result) {
            System.out.println(
                    "Withdrawing ₹" + withdrawalAmount + ": SUCCESS"
            );
            System.out.println(
                    "New balance: ₹" + account5.getBalance()
            );
        } else {
            System.out.println(
                    "Withdrawing ₹" + withdrawalAmount
                    + ": FAILED (Minimum balance violation)"
            );
        }

        System.out.print("After withdrawal: ");
        displayAccount(account5);

        // Invalid withdrawal: would go below ₹1000
        withdrawalAmount = 900.0;

        result = account5.withdraw(
                withdrawalAmount,
                1234
        );

        if (result) {
            System.out.println(
                    "Withdrawing ₹" + withdrawalAmount + ": SUCCESS"
            );
        } else {
            System.out.println(
                    "Withdrawing ₹" + withdrawalAmount
                    + " (would leave ₹"
                    + (account5.getBalance() - withdrawalAmount)
                    + "): FAILED (Minimum balance violation)"
            );
        }

        System.out.println(
                "Current balance: ₹" + account5.getBalance()
        );
        System.out.println();

        
        // Test 6: Account Status Management
        

        System.out.println(">>> Test 6: Account Status Management");

        AccountEnhanced account6 = new AccountEnhanced(
                1006,
                "Charlie Green",
                35,
                2000.0,
                "Savings"
        );

        System.out.print("Initial: ");
        displayAccount(account6);

        // Close account
        boolean closeResult = account6.closeAccount();

        if (closeResult) {
            System.out.println("Closing account: SUCCESS");
        } else {
            System.out.println("Closing account: FAILED");
        }

        System.out.print("After close: ");
        displayAccount(account6);

        System.out.println();

        // Try depositing into closed account
        double depositAmount = 500.0;

        boolean depositResult = account6.deposit(depositAmount);

        if (depositResult) {
            System.out.println(
                    "Depositing ₹" + depositAmount + " to closed account: SUCCESS"
            );
        } else {
            System.out.println(
                    "Depositing ₹" + depositAmount
                    + " to closed account: FAILED (Account inactive)"
            );
        }

        // Reopen account
        boolean reopenResult = account6.reopenAccount();

        if (reopenResult) {
            System.out.println("Reopening account: SUCCESS");
        } else {
            System.out.println("Reopening account: FAILED");
        }

        System.out.print("After reopen: ");
        displayAccount(account6);

        System.out.println();

        
        // Test 7: PIN Protection


        System.out.println(">>> Test 7: PIN Protection");

        AccountEnhanced account7 = new AccountEnhanced(
                1007,
                "Diana Prince",
                28,
                1500.0,
                "Savings"
        );

        // Set PIN
        boolean pinResult = account7.setPin(1234);

        if (pinResult) {
            System.out.println("Setting PIN 1234: SUCCESS");
        } else {
            System.out.println("Setting PIN 1234: FAILED");
        }

        // Correct PIN
        double amount = 200.0;

        boolean withdrawResult = account7.withdraw(
                amount,
                1234
        );

        if (withdrawResult) {
            System.out.println(
                    "Withdrawing ₹" + amount
                    + " with correct PIN (1234): SUCCESS"
            );
            System.out.println(
                    "New balance: ₹" + account7.getBalance()
            );
        } else {
            System.out.println(
                    "Withdrawing ₹" + amount
                    + " with correct PIN (1234): FAILED"
            );
        }

        // Incorrect PIN
        amount = 100.0;

        withdrawResult = account7.withdraw(
                amount,
                9999
        );

        if (withdrawResult) {
            System.out.println(
                    "Withdrawing ₹" + amount
                    + " with incorrect PIN (9999): SUCCESS"
            );
        } else {
            System.out.println(
                    "Withdrawing ₹" + amount
                    + " with incorrect PIN (9999): FAILED (Incorrect PIN)"
            );
        }

        
        // Test account without PIN
        

        AccountEnhanced account8 = new AccountEnhanced(
                1008,
                "No Pin User",
                28,
                1000.0,
                "Savings"
        );

        amount = 100.0;

        withdrawResult = account8.withdraw(
                amount,
                1234
        );

        if (withdrawResult) {
            System.out.println(
                    "Withdrawing ₹" + amount
                    + " with PIN not set: SUCCESS"
            );
        } else {
            System.out.println(
                    "Withdrawing ₹" + amount
                    + " with PIN not set: FAILED (PIN not set)"
            );
        }

        System.out.println();

        
        // Test 8: All Accounts Summary
        

        System.out.println(">>> Test 8: All Accounts Summary");

        displayAccount(account1);
        displayAccount(account2);
        displayAccount(account3);
        displayAccount(account4);
        displayAccount(account5);
        displayAccount(account6);
        displayAccount(account7);

        System.out.println("============================================================");
        System.out.println("ENHANCED TEST COMPLETED!");
        System.out.println("============================================================");
    }
}
