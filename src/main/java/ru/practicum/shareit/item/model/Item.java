package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.utility.Entity;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item extends Entity {

    @NotNull
    private String name;

    private String description;

    private Boolean available;

    private User owner;

    private ItemRequest request;

    public Item(Integer id, String name, String description, Boolean available, User owner, ItemRequest request) {
        super(id);
        if (name == null) {
            throw new ValidationException("Item name can not be null");
        }
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }
}
