package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    private ItemRequestMapper() {
    }

    public static ItemRequest fromDto(RequestItemRequestDto dto, User user) {
        return new ItemRequest(
            null,
            dto.getDescription(),
            LocalDateTime.now(),
            user
        );
    }

    public static ResponseItemRequestDto toDto(ItemRequest itemRequest, List<Item> items) {
        return new ResponseItemRequestDto(
            itemRequest.getId(),
            itemRequest.getDescription(),
            itemRequest.getCreated(),
            items.stream().map(ItemMapper::toNestedDto).collect(Collectors.toList())
        );
    }
}
