package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

    private ItemMapper() {
    }

    public static Item fromDTO(RequestItemDTO dto, User user, ItemRequest request) {
        return new Item(
                null,
                user,
                dto.getName(),
                dto.getDescription(),
                dto.getAvailable(),
                request
        );
    }

    public static NestedItemDTO toNestedDTO(Item item) {
        return new NestedItemDTO(
                item.getId(),
                item.getName(),
                item.getUser() != null ? item.getUser().getId() : null,
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static ResponseItemDTO toDTO(Item item, Booking last, Booking next, List<Comment> comments) {
        return new ResponseItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getUser() != null ? item.getUser().getId() : null,
                item.getRequest() != null ? item.getRequest().getId() : null,
                last != null ? BookingMapper.toNestedDTO(last) : null,
                next != null ? BookingMapper.toNestedDTO(next) : null,
                comments.stream().map(CommentMapper::toNestedDTO).collect(Collectors.toList())
        );
    }
}
