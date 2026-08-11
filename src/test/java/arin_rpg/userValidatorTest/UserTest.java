package arin_rpg.userValidatorTest;


import arin_rpg.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    @DisplayName("Getters e setters de id, userName, email e password devem funcionar corretamente")
    void gettersAndSettersShouldWorkCorrectly() {
        user.setId(1L);
        user.setUserName("arin");
        user.setEmail("arin@teste.com");
        user.setPassword("Senha@123");

        assertEquals(1L, user.getId());
        assertEquals("arin", user.getUserName());
        assertEquals("arin@teste.com", user.getEmail());
        assertEquals("Senha@123", user.getPassword());
    }

    @Test
    @DisplayName("Getter e setter de dateOfBirth devem funcionar corretamente")
    void dateOfBirthGetterAndSetterShouldWorkCorrectly() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        user.setDateOfBirth(birthDate);

        assertEquals(birthDate, user.getDateOfBirth());
    }

    @Test
    @DisplayName("createAt deve ser preenchido automaticamente na criação do objeto")
    void createAtShouldBeSetAutomaticallyOnCreation() {
        LocalDateTime before = LocalDateTime.now();
        User newUser = new User();
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(newUser.getCreateAt());
        assertFalse(newUser.getCreateAt().isBefore(before));
        assertFalse(newUser.getCreateAt().isAfter(after));
    }

    @Test
    @DisplayName("Campos não definidos devem ser nulos por padrão")
    void undefinedFieldsShouldBeNullByDefault() {
        assertNull(user.getId());
        assertNull(user.getUserName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getDateOfBirth());
    }
}
