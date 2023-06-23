package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    private BookingMapper() {
    }

    public static Booking fromDto(BookingDto dto, Item item, User booker) {
        return new Booking(
                null,
                item,
                dto.getStart(),
                dto.getEnd(),
                booker,
                dto.getStatus() == null ? BookingStatus.WAITING : BookingStatus.valueOf(dto.getStatus())
        );
    }

    public static BookingDto toDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                null,
                ItemMapper.toNestedDto(booking.getItem()),
                booking.getStartDate(),
                booking.getEndDate(),
                UserMapper.toNestedDto(booking.getBooker()),
                booking.getStatus().toString()
        );
    }
}
