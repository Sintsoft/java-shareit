package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {

    Integer id;

    @NotNull
    String name;

    String description;

    Boolean avaliable;

    User owner;

    ItemRequest request;
}
