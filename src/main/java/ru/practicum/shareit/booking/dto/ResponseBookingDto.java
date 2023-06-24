package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.NestedItemDto;
import ru.practicum.shareit.user.dto.NestedUserDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBookingDto {

    private Long id;

    private NestedItemDto item;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    private NestedUserDto booker;

    private String status;
}
