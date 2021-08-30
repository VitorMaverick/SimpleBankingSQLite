package banking.controller;

import banking.model.Account;
import banking.repository.AccountRepo;
import banking.util.CheckSum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Moviment {

    public static void addIncome(AccountRepo repo, Account account, int value) {
        account.deposit(value);
        repo.updateBalance(account, account.getBalance());
    }


    public static boolean transfer(AccountRepo repo, Account sender, String cardNumber) {
        Scanner scanner = new Scanner(System.in);

        if (!CheckSum.check(cardNumber)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            return false;
        }
        Account recipient = repo.queryByNumber(cardNumber);

        if (recipient != null) {
            System.out.println("Enter how much money you want to transfer:");
            int value = scanner.nextInt();
            if (sender.retirada(value)) {
                recipient.deposit(value);
                repo.transfer(sender, recipient);
                System.out.println("Success!");
                return true;
            } else {
                System.out.println("Not enough money!");
                return false;
            }

        } else {
            System.out.println("Such a card does not exist");
            return false;
        }

    }

}
