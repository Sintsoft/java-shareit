package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NestedItemDto;
import ru.practicum.shareit.user.dto.NestedUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long itemId;

    private NestedItemDto item;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    private NestedUserDto booker;

    private String status;
}
