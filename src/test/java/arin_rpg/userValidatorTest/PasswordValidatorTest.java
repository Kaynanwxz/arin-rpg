package arin_rpg.userValidatorTest;

import arin_rpg.utils.PasswordValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordValidatorTest {

    @Test
    @DisplayName("Senha válida com maiúscula, minúscula, número e caractere especial deve passar")
    void validPasswordShouldReturnTrue() {
        assertTrue(PasswordValidator.isValid("Senha@123"));
    }

    @Test
    @DisplayName("Senha válida com todos os tipos de caracteres especiais aceitos")
    void validPasswordWithDifferentSpecialCharsShouldReturnTrue() {
        assertTrue(PasswordValidator.isValid("Abcdef1$"));
        assertTrue(PasswordValidator.isValid("Abcdef1!"));
        assertTrue(PasswordValidator.isValid("Abcdef1%"));
        assertTrue(PasswordValidator.isValid("Abcdef1*"));
        assertTrue(PasswordValidator.isValid("Abcdef1?"));
        assertTrue(PasswordValidator.isValid("Abcdef1&"));
        assertTrue(PasswordValidator.isValid("Abcdef1."));
        assertTrue(PasswordValidator.isValid("Abcdef1#"));
        assertTrue(PasswordValidator.isValid("Abcdef1_"));
        assertTrue(PasswordValidator.isValid("Abcdef1-"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Senha nula ou vazia deve retornar false")
    void nullOrEmptyPasswordShouldReturnFalse(String password) {
        assertFalse(PasswordValidator.isValid(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "senha@123",     // sem maiúscula
            "SENHA@123",     // sem minúscula
            "Senha@abc",     // sem número
            "Senha1234",     // sem caractere especial
            "Se@1a",         // menos de 8 caracteres
            "        ",      // apenas espaços
            "Senha 123@"     // contém espaço (não permitido pela regex)
    })
    @DisplayName("Senhas que não atendem aos requisitos devem retornar false")
    void invalidPasswordsShouldReturnFalse(String password) {
        assertFalse(PasswordValidator.isValid(password));
    }

    @Test
    @DisplayName("Senha com exatamente 8 caracteres válidos deve passar (limite mínimo)")
    void passwordWithExactlyMinimumLengthShouldReturnTrue() {
        assertTrue(PasswordValidator.isValid("Ab1@abcd"));
    }

    @Test
    @DisplayName("Senha muito longa mas válida deve passar")
    void longValidPasswordShouldReturnTrue() {
        assertTrue(PasswordValidator.isValid("Abcdefghijklmnop1@"));
    }
}