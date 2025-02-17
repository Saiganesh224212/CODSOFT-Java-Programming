
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class task3 {
    // Represents a bank account with transactions
    static class BankAccount {
        private double balance;

        public BankAccount(double initialBalance) {
            this.balance = initialBalance;
        }

        public void deposit(double amount) {
            if (amount > 0) {
                balance += amount;
                System.out.println("✅ ₹" + amount + " deposited successfully.");
            } else {
                System.out.println("❌ Invalid deposit amount.");
            }
        }

        public void withdraw(double amount) {
            if (amount > 0 && amount <= balance) {
                balance -= amount;
                System.out.println("✅ ₹" + amount + " withdrawn successfully.");
            } else {
                System.out.println("❌ Insufficient funds or invalid amount.");
            }
        }

        public double getBalance() {
            return balance;
        }
    }

    // User authentication model
    static class User {
        private final String cardNumber;
        private final int pin;

        public User(String cardNumber, int pin) {
            this.cardNumber = cardNumber;
            this.pin = pin;
        }

        public boolean authenticate(String enteredCardNumber, int enteredPin) {
            return this.cardNumber.equals(enteredCardNumber) && this.pin == enteredPin;
        }
    }

    // Handles authentication and bank accounts
    static class Bank {
        private final Map<String, User> users = new HashMap<>();
        private final Map<String, BankAccount> accounts = new HashMap<>();

        public Bank() {
            users.put("123456", new User("123456", 1234));
            accounts.put("123456", new BankAccount(10000));

            users.put("654321", new User("654321", 4321));
            accounts.put("654321", new BankAccount(5000));
        }

        public BankAccount authenticateUser(String cardNumber, int pin) {
            User user = users.get(cardNumber);
            return (user != null && user.authenticate(cardNumber, pin)) ? accounts.get(cardNumber) : null;
        }
    }

    // Handles ATM operations
    static class ATM {
        private final BankAccount account;

        public ATM(BankAccount account) {
            this.account = account;
        }

        public void deposit(double amount) {
            account.deposit(amount);
        }

        public void withdraw(double amount) {
            account.withdraw(amount);
        }

        public void displayBalance() {
            System.out.println("💰 Current Balance: ₹" + account.getBalance());
        }
    }

    // UI logic and user interaction
    static class ATMInterface {
        private final Scanner scanner;
        private final Bank bank;

        public ATMInterface() {
            scanner = new Scanner(System.in);
            bank = new Bank();
        }

        public void runATM() {
            System.out.println("\n💳 Welcome to the ATM!");
            System.out.print("🔹 Enter your card number: ");
            String cardNumber = scanner.next();

            System.out.print("🔹 Enter your PIN: ");
            int pin = scanner.nextInt();

            BankAccount userAccount = bank.authenticateUser(cardNumber, pin);

            if (userAccount == null) {
                System.out.println("❌ Invalid Card Number or PIN.");
                scanner.close();
                return;
            }

            ATM atm = new ATM(userAccount);
            System.out.println("✅ Authentication successful!\n");

            while (true) {
                System.out.println("\n--- ATM MENU ---");
                System.out.println("1️⃣ Check Balance");
                System.out.println("2️⃣ Deposit Money");
                System.out.println("3️⃣ Withdraw Money");
                System.out.println("4️⃣ Exit");
                System.out.print("🔹 Choose an option: ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> atm.displayBalance();
                    case 2 -> {
                        System.out.print("💰 Enter deposit amount: ₹");
                        double depositAmount = scanner.nextDouble();
                        atm.deposit(depositAmount);
                    }
                    case 3 -> {
                        System.out.print("💸 Enter withdrawal amount: ₹");
                        double withdrawAmount = scanner.nextDouble();
                        atm.withdraw(withdrawAmount);
                    }
                    case 4 -> {
                        System.out.println("🔒 Thank you for using the ATM. Goodbye!");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice. Please try again.");
                }
            }
        }
    }
    public static void main(String[] args) {
        new ATMInterface().runATM();
    }
}
