package arin_rpg.userValidatorTest;


import arin_rpg.utils.EmailValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Observação: EmailValidator.domainExists() faz uma consulta DNS (MX) real,
 * então os testes "positivos" abaixo dependem de conexão de rede disponível
 * e podem ser marcados com a tag "network" para serem excluídos em ambientes
 * sem acesso à internet (ex: pipelines de CI isolados).
 */
class EmailValidatorTest {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Email nulo ou vazio deve retornar false")
    void nullOrEmptyEmailShouldReturnFalse(String email) {
        assertFalse(EmailValidator.domainExists(email));
    }

    @Test
    @DisplayName("Email em branco (apenas espaços) deve retornar false")
    void blankEmailShouldReturnFalse() {
        assertFalse(EmailValidator.domainExists("   "));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "dominio-invalido-que-nao-existe-123456789.com",
            "teste@dominio-invalido-que-nao-existe-987654321xyz.com"
    })
    @Tag("network")
    @DisplayName("Email com domínio inexistente deve retornar false")
    void nonExistentDomainShouldReturnFalse(String email) {
        assertFalse(EmailValidator.domainExists(email));
    }

    @Test
    @Tag("network")
    @DisplayName("Email com domínio conhecido e com registro MX válido deve retornar true")
    void validKnownDomainShouldReturnTrue() {
        assertTrue(EmailValidator.domainExists("teste@gmail.com"));
    }

    @Test
    @DisplayName("Email sem '@' deve lançar exceção interna tratada e retornar false")
    void emailWithoutAtSymbolShouldReturnFalse() {
        // indexOf("@") retorna -1, então substring(0) devolve a string inteira,
        // resultando em uma consulta DNS a um domínio inválido -> false
        assertFalse(EmailValidator.domainExists("emailinvalidosemarroba"));
    }
}
