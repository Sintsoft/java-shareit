package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    private ItemMapper() {
    }

    public static Item fromDto(ItemDto dto) {
        return new Item(
                dto.getId(),
                null,
                dto.getName(),
                dto.getDescription(),
                dto.getAvailable(),
                null
        );
    }

    public static Item updateFromDto(Item updateItem, ItemDto dto) {
        return new Item(
                updateItem.getId(),
                updateItem.getUser(),
                dto.getName() != null
                        && !updateItem.getName().equals(dto.getName())
                        ? dto.getName() : updateItem.getName(),
                dto.getDescription() != null
                        && !updateItem.getDescription().equals(dto.getDescription())
                        ? dto.getDescription() : updateItem.getDescription(),
                dto.getAvailable() != null
                        && !updateItem.getAvailable().equals(dto.getAvailable())
                        ? dto.getAvailable() : updateItem.getAvailable(),
                updateItem.getRequest()
        );
    }

    public static ItemDto toDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getUser() != null ? item.getUser().getId() : null,
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static NestedItemDto toNestedDto(Item item) {
        return new NestedItemDto(
                item.getId(),
                item.getName()
        );
    }
}
