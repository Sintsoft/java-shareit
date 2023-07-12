package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

public abstract class ItemMapper {

    public static Item fromDto(RequestItemDTO dto, User owner, ItemRequest request) {
        return new Item(
                null,
                dto.getName(),
                dto.getDescription(),
                dto.getAvailable() == null || dto.getAvailable(),
                owner,
                request
        );
    }


    public static NestedItemDTO toNested(Item item) {
        return new NestedItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription()
        );
    }

    public static ResponseItemDTO toDto(Item item) {
        return new ResponseItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }
}
