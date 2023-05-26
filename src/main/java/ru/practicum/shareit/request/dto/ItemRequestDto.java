package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
public class ItemRequestDto {

    Integer id;

    String description;

    Integer requestor;

    LocalDateTime created;
}
