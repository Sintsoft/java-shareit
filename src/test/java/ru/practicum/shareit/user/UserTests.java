package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;

import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTests {

    @Test
    void createUserTest() {
        assertDoesNotThrow(() -> {
            User user = new User(
                null,
                "name",
                "user@email.com"
            );
        });

    }

    @Test
    void createNullNameUserTest() {
        assertThrows(ValidationException.class, () -> {
            User user = new User(
                null,
                null,
                "email"
                );
            }
        );
    }

    @Test
    void createNullEmailUserTest() {
        assertThrows(ValidationException.class, () -> {
            User user = new User(
                        null,
                        "name",
                        null
                );
            }
        );
    }

    @Test
    void createIncorrectEmailUserTest() {
        assertThrows(ValidationException.class, () -> {
            User user = new User(
                        null,
                        "name",
                        "email"
                );
            }
        );
    }
}
