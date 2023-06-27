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

    public static Booking fromDto(RequestBookingDto dto, Item item, User booker) {
        return new Booking(
                null,
                item,
                dto.getStart(),
                dto.getEnd(),
                booker,
                BookingStatus.WAITING
        );
    }

    public static NestedBookingDto toNested(Booking booking) {
        return new NestedBookingDto(
            booking.getId(),
            booking.getBooker().getId()
        );
    }

    public static ResponseBookingDto toDto(Booking booking) {
        return new ResponseBookingDto(
                booking.getId(),
                ItemMapper.toNestedDto(booking.getItem()),
                booking.getStart(),
                booking.getEnd(),
                UserMapper.toNestedDto(booking.getBooker()),
                booking.getStatus().toString()
        );
    }
}
