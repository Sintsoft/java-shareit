package ru.practicum.shareit.item.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.NestedItemRequestDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseItemDTO {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;
}
