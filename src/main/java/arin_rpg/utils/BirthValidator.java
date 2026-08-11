package arin_rpg.utils;

import java.time.LocalDate;
import java.time.Period;

public class BirthValidator {

    public static boolean isValid(LocalDate date) {
        if (date == null) return false;

        return Period.between(date, LocalDate.now()).getYears() >= 18;
    }
}
