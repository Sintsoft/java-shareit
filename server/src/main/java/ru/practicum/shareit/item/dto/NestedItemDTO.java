package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NestedItemDTO {

    private Long id;

    private String name;

    private Long userId;

    private String description;

    private Boolean available;

    private Long requestId;
}
