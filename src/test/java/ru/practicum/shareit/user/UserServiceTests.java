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
                        new UserDto(null, "name", "myemail@mail.ru")
                );
        });
        assertEquals(2, testUserService.getAllUsers().size());
    }

    @Test
    @Order(2)
    void getUserTests() {
        UserDto testDto = new UserDto(1, "name", "email@mail.ru");
        assertTrue(testDto.equals(testUserService.getUser(1)));
    }

    @Test
    @Order(3)
    void updateUserTests() {
        UserDto testDto = new UserDto(null, "newname", "newemail@mail.ru");
        testUserService.updateUser(1, testDto);
        testDto.setId(1);
        assertTrue(testDto.equals(testUserService.getUser(1)));
    }

    @Test
    @Order(4)
    void deleteUserTest() {
        assertDoesNotThrow(() -> {
            testUserService.removeUser(1);
        });
        assertEquals(1, testUserService.getAllUsers().size());
    }
}
