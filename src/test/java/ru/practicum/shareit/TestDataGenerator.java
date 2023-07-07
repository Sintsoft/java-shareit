package ru.practicum.shareit;

import ru.practicum.shareit.user.model.User;

public abstract class TestDataGenerator {

    public static User generateTestUser(Long id) {
        return new User(
                id,
                "user" + 1,
                "user" + 1 + "@email.com"
        );
    }
}
