package ru.practicum.shareit.booking;


import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.Validator;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static javax.validation.Validation.buildDefaultValidatorFactory;

public class BookingModelTests {

    public static Validator validator = buildDefaultValidatorFactory().getValidator();

    public static Booking getValidTestBooking() {
        return new Booking(
                null,
                new Item(
                        1L,
                        new User(
                                1L,
                                "name",
                                "mail@email.ru"
                        ),
                        "item",
                        "description",
                        true,
                        null
                ),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                new User(
                        2L,
                        "name2",
                        "mail2@email.ru"
                ),
                BookingStatus.WAITING
        );
    }

    @Test
    void createBookingTest() {
        Booking testBooking = getValidTestBooking();
        assertEquals(0, validator.validate(testBooking).size());
    }

    @Test
    void mapBookingToDtoTest() {
        ResponseBookingDto testDTO = BookingMapper.toDto(getValidTestBooking());
        assertEquals(null, testDTO.getId());
        assertEquals(2, testDTO.getBooker().getId());
        assertEquals(1, testDTO.getItem().getId());
        assertEquals(1, testDTO.getItem().getUserId());
        assertEquals(BookingStatus.WAITING.toString(), testDTO.getStatus());
    }

    @Test
    void mapDtoToBookingTest() {
        RequestBookingDto testDTO = new RequestBookingDto(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        );
        Booking testBooking = BookingMapper.fromDto(
                testDTO,
                new Item(
                        1L,
                        new User(
                                1L,
                                "name",
                                "mail@email.ru"
                        ),
                        "item",
                        "description",
                        true,
                        null
                ),
                new User(
                        2L,
                        "name2",
                        "mail2@email.ru"
                ));

        assertEquals(BookingStatus.WAITING, testBooking.getStatus());
    }
}
