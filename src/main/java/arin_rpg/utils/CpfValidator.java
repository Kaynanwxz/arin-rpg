package arin_rpg.utils;

public class CpfValidator {

    public static boolean isValid(String cpf) {
        if (cpf == null) return false;

        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) return false;

        if (cpf.matches("(\\d)\\1{10}")) return false;

        try {
            int[] numbers = cpf.chars()
                    .map(Character::getNumericValue)
                    .toArray();

            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += numbers[i] * (10 - i);
            }

            int digit1 = (sum * 10) % 11;
            if (digit1 == 10) digit1 = 0;

            if (digit1 != numbers[9]) return false;

            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += numbers[i] * (11 - i);
            }

            int digit2 = (sum * 10) % 11;
            if (digit2 == 10) digit2 = 0;

            return digit2 == numbers[10];

        } catch (Exception e) {
            return false;
        }
    }

}
