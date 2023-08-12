package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.NestedItemDTO;
import ru.practicum.shareit.user.dto.NestedUserDTO;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBookingDTO {

    private Long id;

    private NestedItemDTO item;

    private LocalDateTime start;

    private LocalDateTime end;

    private NestedUserDTO booker;

    private String status;
}
