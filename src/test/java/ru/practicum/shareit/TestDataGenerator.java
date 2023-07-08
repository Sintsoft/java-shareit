package ru.practicum.shareit;

import ru.practicum.shareit.user.dto.RequestUserDTO;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.model.User;

public abstract class TestDataGenerator {

    /*
    *   User test data generation block
    */

    public static User generateTestUser(Long id) {
        return new User(
                id,
                "user" + id,
                "user" + id + "@email.com"
        );
    }

    public static RequestUserDTO generateTestRequestUserDTO(long id) {
        return new RequestUserDTO(
                "user" + id,
                "user" + id + "@email.com"
        );
    }

    public static ResponseUserDto generateTestResponseUserDTO(Long id) {
        return new ResponseUserDto(
                id,
                "user" + id,
                "user" + id + "@email.com"
        );
    }
}
