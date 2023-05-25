package ru.practicum.shareit.user;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTests {

    @Autowired
    UserService testUserService;

    @Test
    @Order(1)
    void addUserTests() {
        assertDoesNotThrow(() -> {
                testUserService.addUser(
                        new UserDto(null, "name", "email")
                );
        });

    }
}
