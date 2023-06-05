package ru.practicum.shareit.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public abstract class Entity {

    private Integer id;
}
