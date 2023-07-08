package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.NestedBookingDto;
import ru.practicum.shareit.comment.dto.NestedCommentDto;

import java.util.List;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ResponseItemDto {

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

    // @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long requestId;

    // @JsonInclude(JsonInclude.Include.NON_NULL)
    private NestedBookingDto lastBooking;

    // @JsonInclude(JsonInclude.Include.NON_NULL)
    private NestedBookingDto nextBooking;

    private List<NestedCommentDto> comments;
}
