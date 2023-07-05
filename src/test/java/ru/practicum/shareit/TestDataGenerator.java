package ru.practicum.shareit;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public abstract class TestDataGenerator {

//    Users generation block

    public static User generateTestUser(long id) {
        return new User(
                id,
                "user" + id,
                "user" + id + "@mail.ru"
        );
    }


//    Items generations block
    public static Item generateTestItem(long id, long ownerid) {
        return new Item(
                id,
                generateTestUser(ownerid),
                "item" + id,
                "item" + id + " description",
                true,
                null
        );
    }

//    Requests generation block
    public static ItemRequest generateTestItemRequest(long id, long userId) {
        return new ItemRequest(
                id,
                "request" + id,
                LocalDateTime.now(),
                generateTestUser(userId)
        );
    }
}
