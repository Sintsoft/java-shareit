package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.NestedItemDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ResponseItemRequestDTO {

    private Long id;

    private String description;

    private LocalDateTime created;

    private List<NestedItemDTO> items;

}