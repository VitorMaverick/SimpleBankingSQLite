package banking.util;

public class CheckSum {
    public static boolean check(String number) {
        int sum = 0;
        int checkSum = 0;
        int luh;
        char aux = number.charAt(15);
        int pretendToBe = Character.getNumericValue(aux);

        // #3: removed pos
        for (int i = 1; i < number.length(); i++) {
            char num = number.charAt(i - 1);
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

        if (pretendToBe == checkSum) {
            return true;
        } else {
            return false;
        }
    }
}
