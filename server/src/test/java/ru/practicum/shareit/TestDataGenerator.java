package ru.practicum.shareit;

import ru.practicum.shareit.user.dto.RequestUserDTO;
import ru.practicum.shareit.user.dto.ResponseUserDTO;
import ru.practicum.shareit.user.model.User;

public class TestDataGenerator {

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

    public static ResponseUserDTO generateTestResponseUserDTO(long id) {
        return new ResponseUserDTO(
                id,
                "user" + id,
                "user" + id + "@email.com"
        );
    }

    /*
     * Item test data deneration block
     */
}
