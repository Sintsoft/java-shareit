package ru.practicum.shareit.utility.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Component
public class AppStorage {

    public final CRUDStorage<User> UserStorage = new CRUDStorage<>();

    public final CRUDStorage<Item> ItemStorage = new CRUDStorage<>();

}