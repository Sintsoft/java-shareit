package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.constraints.NotNull;

@Service
public interface BookingService {

    BookingDto createBooking(BookingDto dto, Long userId);

    BookingDto approveBooking(long bookingId, boolean approved, long userId);

    Booking loadBooking(long bookingId);
}
