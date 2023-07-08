package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.user.dto.NestedUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.validation.Validator;

import static javax.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.*;
public class UserModelTests {

    public static Validator validator = buildDefaultValidatorFactory().getValidator();

    @Test
    void userCreationTest() {
        User testUser = new User(
                null,
                "user",
                "user@email.com"
        );

        assertEquals(0, validator.validate(testUser).size());
    }

    @Test
    void ivalidUserAnnotationTest() {
        User testUser = new User(
                null,
                null,
                "useremailcom"
        );

        assertEquals(2, validator.validate(testUser).size());
    }

    @Test
    void userFromDtoTest() {
        UserDto testDTO = new UserDto(null, "user", "user@email.com");
        User testUser = UserMapper.fromDto(testDTO);

        assertEquals(0, validator.validate(testUser).size());
        assertEquals("user", testUser.getName());
        assertEquals("user@email.com", testUser.getEmail());
        assertNull(testUser.getId());
    }

    @Test
    void userToDtoTest() {
        UserDto testDto = UserMapper.toDto(TestDataGenerator.generateTestUser(1L));
        NestedUserDto testNestedDto = UserMapper.toNestedDto(TestDataGenerator.generateTestUser(1L));

        assertEquals("user1", testDto.getName());
        assertEquals("user1@mail.ru", testDto.getEmail());
        assertEquals(1L, testDto.getId());
        assertEquals(1L, testNestedDto.getId());
    }
}
