package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.RequestUserDTO;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;
public class UserModelTests {

    @Test
    void createUserTest() {
        assertDoesNotThrow(() -> {
            new User(null, "user1", "user1@email.com");
        });
    }

    @Test
    void createRequestDtoTest() {
        assertDoesNotThrow(() -> {
            new RequestUserDTO(null, null, null);
        });
    }
}
