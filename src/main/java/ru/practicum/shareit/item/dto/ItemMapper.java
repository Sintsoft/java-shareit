package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInvalidEntity;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

    private ItemMapper() {
    }

    public static Item fromDto(RequestItemDto dto) {
        if (dto.getName().isBlank() || dto.getAvailable() == null || dto.getDescription().isBlank()) {
            throw new ShareItInvalidEntity("Blank fields in item.");
        }
        return new Item(
                null,
                null,
                dto.getName(),
                dto.getDescription(),
                dto.getAvailable(),
                null
        );
    }

    public static Item updateFromDto(Item updateItem, RequestItemDto dto) {
        return new Item(
                updateItem.getId(),
                updateItem.getUser(),
                dto.getName() != null
                        && !updateItem.getName().equals(dto.getName())
                        ? dto.getName() : updateItem.getName(),
                dto.getDescription() != null
                        && !updateItem.getDescription().equals(dto.getDescription())
                        ? dto.getDescription() : updateItem.getDescription(),
                dto.getAvailable() != null
                        && !updateItem.getAvailable().equals(dto.getAvailable())
                        ? dto.getAvailable() : updateItem.getAvailable(),
                updateItem.getRequest()
        );
    }

    public static ResponseItemDto toDto(Item item, Booking last, Booking next, List<Comment> comments) {
        return new ResponseItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getUser() != null ? item.getUser().getId() : null,
                item.getRequest() != null ? item.getRequest().getId() : null,
                last != null ? BookingMapper.toNested(last) : null,
                next != null ? BookingMapper.toNested(next) : null,
                comments.stream().map(CommentMapper::toNested).collect(Collectors.toList())

        );
    }

    public static NestedItemDto toNestedDto(Item item) {
        return new NestedItemDto(
                item.getId(),
                item.getName(),
                item.getUser() != null ? item.getUser().getId() : null,
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }
}
