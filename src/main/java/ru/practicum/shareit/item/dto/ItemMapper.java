package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    private ItemMapper() {
    }

    public static ItemDto toDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable() == null ? true : item.getAvailable(),
                item.getOwner() != null ? item.getOwner().getId() : null,
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item fromDto(ItemDto dto) {
        return new Item(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getAvailable(),
                null,
                null
        );
    }
}
