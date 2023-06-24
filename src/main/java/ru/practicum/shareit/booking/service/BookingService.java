package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;

import java.util.List;


@Service
public interface BookingService {

    ResponseBookingDto createBooking(RequestBookingDto dto, Long userId);

    ResponseBookingDto approveBooking(long bookingId, boolean approved, long userId);

    ResponseBookingDto getBooking(Long bookingId, Long userId);

    List<ResponseBookingDto> getUserBookings(Long userId);

    public List<ResponseBookingDto> getUserItemsBookings(Long userId);
}
