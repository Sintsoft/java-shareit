package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemDto {

    Integer id;

    @NotNull
    String name;

    String description;

    Boolean avaliable;

    Integer owner;

    Integer request;
}
