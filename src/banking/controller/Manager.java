package banking.controller;

import banking.model.Account;
import banking.repository.AccountRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Manager {
    List<Account> accounts = new ArrayList<Account>();

    public Account registerAccount(AccountRepo ac) {
        Account account = new Account(generateId(), generateCard());
        accounts.add(account);

        ac.put(account);

        return account;
    }

    public int checkSum(String formated) {
        int sum = 0;
        int checkSum = 0;
        int luh;

        // #3: removed pos
        for (int i = 1; i <= formated.length(); i++) {
            char num = formated.charAt(i - 1);
            // #1: fixed the '0' problem
            int tmp = Character.getNumericValue(num);
            int product;

            if (i % 2 != 0) {
                product = tmp * 2;
            } else {
                product = tmp * 1;
            }
            if (product > 9)
                product -= 9;
            sum += product;

        }

        luh = sum % 10;
        if (luh != 0) {
            checkSum = 10 - luh;
        }


        return checkSum;
    }

    public String generateCard() {
        final int bin = 400000;
        double next = ThreadLocalRandom.current().nextDouble(999999999);
        String formated = String.format("%d%09.0f", bin, next);

        int checkSum = checkSum(formated);

        String finalNumber = String.format("%s%d", formated, checkSum);

        for (Account ac : accounts) {
            while (ac.compareCard(finalNumber)) {
                next = ThreadLocalRandom.current().nextDouble(999999999);
                formated = String.format("%d%09.0f", bin, next);
                checkSum = checkSum(formated);
                finalNumber = String.format("%s%d", formated, checkSum);
            }
        }

        return finalNumber;
    }

    public String generateId() {
        int next = ThreadLocalRandom.current().nextInt(9999);
        String formated = String.format("%04d", next);

        for (Account ac : accounts) {
            while (ac.compareId(formated)) {
                next = ThreadLocalRandom.current().nextInt(9999);
                formated = String.format("%04d", next);
            }
        }

        return formated;
    }


    public Account login(AccountRepo repo, double card, int id) {
        String formatedId = String.format("%04d", id);
        String formatedCard = String.format("%09.0f", card);
        Account account = repo.auth(formatedId, formatedCard);
        if (account != null) {
            return account;
        }
        return null;
    }

}
