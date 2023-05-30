package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;

import javax.validation.Validator;

import static javax.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    Validator validator = buildDefaultValidatorFactory().getValidator();

    @Test
    void createUserTest() {
        assertDoesNotThrow(() -> {
            User user = new User(
                null,
                "name",
                "user@email.com"
            );
            assertTrue(validator.validate(user).isEmpty());
        });

    }

    @Test
    void createNullNameUserTest() {
        User user = new User(
                null,
                null,
                "email"
        );
        assertFalse(validator.validate(user).isEmpty());
    }

    @Test
    void createNullEmailUserTest() {
        User user = new User(
                null,
                "name",
                null
        );
        assertFalse(validator.validate(user).isEmpty());
    }

    @Test
    void createIncorrectEmailUserTest() {
        User user = new User(
                null,
                "name",
                "email.com"
        );
        assertFalse(validator.validate(user).isEmpty());
    }
}
