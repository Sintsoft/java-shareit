package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ItemRequestMapper {

    public static ItemRequest fromDTO(RequestItemRequestDTO dto, User user) {
        return new ItemRequest(
                null,
                dto.getDescription(),
                LocalDateTime.now(),
                user
        );
    }

    public static ResponseItemRequestDTO toDTO(ItemRequest itemRequest, List<Item> items) {
        return new ResponseItemRequestDTO(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                items.stream().map(ItemMapper::toNestedDTO).collect(Collectors.toList())
        );
    }
}
