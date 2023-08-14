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

    public static Booking fromDTO(RequestBookingDTO dto, User user, Item item) {
        return new Booking(
                null,
                item,
                dto.getStart(),
                dto.getEnd(),
                user,
                BookingStatus.WAITING
        );
    }

    public static NestedBookingDTO toNestedDTO(Booking booking) {
        return new NestedBookingDTO(
                booking.getId(),
                booking.getBooker().getId()
        );
    }

    public static ResponseBookingDTO toDTO(Booking booking) {
        return new ResponseBookingDTO(
                booking.getId(),
                ItemMapper.toNestedDTO(booking.getItem()),
                booking.getStart(),
                booking.getEnd(),
                UserMapper.toNestedDTO(booking.getBooker()),
                booking.getStatus().toString()
        );
    }
}
