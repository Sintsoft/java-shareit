package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.user.dto.RequestUserDTO;
import ru.practicum.shareit.user.dto.ResponseUserDTO;
import ru.practicum.shareit.user.dto.UserMapper;
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
            new RequestUserDTO("user1", "user1@email.com");
        });
    }

    @Test
    void createResposeDtoTest() {
        assertDoesNotThrow(() -> {
            new User(1L, "user1", "user1@email.com");
        });
    }

    @Test
    void mapUserFtomDTO() {
        User testUser = UserMapper.fromDto(TestDataGenerator.generateTestRequestUserDTO(1L));
        assertNull(testUser.getId());
        assertEquals("user1", testUser.getName());
        assertEquals("user1@email.com", testUser.getEmail());
    }

    @Test
    void mapUserToDTO() {
        ResponseUserDTO testUserDTO = UserMapper.toDto(TestDataGenerator.generateTestUser(1L));
        assertEquals(1L, testUserDTO.getId());
        assertEquals("user1", testUserDTO.getName());
        assertEquals("user1@email.com", testUserDTO.getEmail());
    }

    @Test
    void updateUserFromDtoTest() {
        User testUser = UserMapper.updateFromDto(
                TestDataGenerator.generateTestUser(1L),
                TestDataGenerator.generateTestRequestUserDTO(3L));
        assertEquals(1L, testUser.getId());
        assertEquals("user3", testUser.getName());
        assertEquals("user3@email.com", testUser.getEmail());

        testUser = UserMapper.updateFromDto(
                TestDataGenerator.generateTestUser(1L),
                new RequestUserDTO(
                        "user1",
                        "user3@email.com"
                ));

        assertEquals(1L, testUser.getId());
        assertEquals("user1", testUser.getName());
        assertEquals("user3@email.com", testUser.getEmail());

        testUser = UserMapper.updateFromDto(
                TestDataGenerator.generateTestUser(1L),
                new RequestUserDTO(
                        "user3",
                        "user1@email.com"
                ));

        assertEquals(1L, testUser.getId());
        assertEquals("user3", testUser.getName());
        assertEquals("user1@email.com", testUser.getEmail());

        testUser = UserMapper.updateFromDto(
                TestDataGenerator.generateTestUser(1L),
                new RequestUserDTO(
                        null,
                        null
                ));

        assertEquals(1L, testUser.getId());
        assertEquals("user1", testUser.getName());
        assertEquals("user1@email.com", testUser.getEmail());

    }
}
