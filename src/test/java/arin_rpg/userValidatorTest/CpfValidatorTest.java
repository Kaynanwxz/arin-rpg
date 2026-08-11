package arin_rpg.userValidatorTest;


import arin_rpg.utils.CpfValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CpfValidatorTest {

    @Test
    @DisplayName("CPF válido sem formatação deve retornar true")
    void validCpfWithoutFormattingShouldReturnTrue() {
        assertTrue(CpfValidator.isValid("52998224725"));
    }

    @Test
    @DisplayName("CPF válido formatado (com pontos e traço) deve retornar true")
    void validCpfWithFormattingShouldReturnTrue() {
        assertTrue(CpfValidator.isValid("529.982.247-25"));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("CPF nulo deve retornar false")
    void nullCpfShouldReturnFalse(String cpf) {
        assertFalse(CpfValidator.isValid(cpf));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "123", "1234567890", "123456789012"})
    @DisplayName("CPF com tamanho diferente de 11 dígitos deve retornar false")
    void cpfWithWrongLengthShouldReturnFalse(String cpf) {
        assertFalse(CpfValidator.isValid(cpf));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "00000000000", "11111111111", "22222222222", "33333333333",
            "44444444444", "55555555555", "66666666666", "77777777777",
            "88888888888", "99999999999"
    })
    @DisplayName("CPF com todos os dígitos iguais deve retornar false")
    void cpfWithAllSameDigitsShouldReturnFalse(String cpf) {
        assertFalse(CpfValidator.isValid(cpf));
    }

    @Test
    @DisplayName("CPF com dígitos verificadores incorretos deve retornar false")
    void cpfWithInvalidCheckDigitsShouldReturnFalse() {
        assertFalse(CpfValidator.isValid("52998224700"));
    }

    @Test
    @DisplayName("CPF com caracteres não numéricos deve ser limpo e validado corretamente")
    void cpfWithNonNumericCharactersShouldBeSanitized() {
        assertTrue(CpfValidator.isValid("529.982.247-25"));
        assertFalse(CpfValidator.isValid("abc.def.ghi-jk"));
    }
}
