package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.NestedItemDTO;
import ru.practicum.shareit.user.dto.NestedUserDTO;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBookingDTO {

    private Long id;

    private NestedItemDTO item;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    private NestedUserDTO booker;

    private String status;
}
