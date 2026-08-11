package arin_rpg.userValidatorTest;


import arin_rpg.utils.BirthValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BirthValidatorTest {

    @Test
    @DisplayName("Data nula deve retornar false")
    void nullDateShouldReturnFalse() {
        assertFalse(BirthValidator.isValid(null));
    }

    @Test
    @DisplayName("Data de nascimento de exatamente 18 anos atrás deve retornar true")
    void exactlyEighteenYearsOldShouldReturnTrue() {
        LocalDate eighteenYearsAgo = LocalDate.now().minusYears(18);
        assertTrue(BirthValidator.isValid(eighteenYearsAgo));
    }

    @Test
    @DisplayName("Data de nascimento de mais de 18 anos deve retornar true")
    void moreThanEighteenYearsOldShouldReturnTrue() {
        LocalDate thirtyYearsAgo = LocalDate.now().minusYears(30);
        assertTrue(BirthValidator.isValid(thirtyYearsAgo));
    }

    @Test
    @DisplayName("Data de nascimento de um dia antes de completar 18 anos deve retornar false")
    void oneDayBeforeTurningEighteenShouldReturnFalse() {
        LocalDate almostEighteen = LocalDate.now().minusYears(18).plusDays(1);
        assertFalse(BirthValidator.isValid(almostEighteen));
    }

    @Test
    @DisplayName("Data de nascimento de menor de idade deve retornar false")
    void underageShouldReturnFalse() {
        LocalDate tenYearsAgo = LocalDate.now().minusYears(10);
        assertFalse(BirthValidator.isValid(tenYearsAgo));
    }

    @Test
    @DisplayName("Data de nascimento no futuro deve retornar false")
    void futureDateShouldReturnFalse() {
        LocalDate futureDate = LocalDate.now().plusYears(1);
        assertFalse(BirthValidator.isValid(futureDate));
    }
}
