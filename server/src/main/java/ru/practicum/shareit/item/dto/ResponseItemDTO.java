package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.booking.dto.NestedBookingDTO;
import ru.practicum.shareit.comment.dto.NestedCommentDTO;

import java.util.List;

@Data
@AllArgsConstructor
public class ResponseItemDTO {

    @NonNull
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private Boolean available;

    @NonNull
    private Long owner;

    private Long requestId;

    private NestedBookingDTO lastBooking;

    private NestedBookingDTO nextBooking;

    private List<NestedCommentDTO> comments;
}