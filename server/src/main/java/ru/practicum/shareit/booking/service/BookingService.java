package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.RequestBookingDTO;
import ru.practicum.shareit.booking.dto.ResponseBookingDTO;

import java.util.List;

@Service
public interface BookingService {

    ResponseBookingDTO createBooking(RequestBookingDTO dto, Long userId);

    ResponseBookingDTO approveBooking(long bookingId, boolean approved, long userId);

    ResponseBookingDTO getBooking(Long bookingId, Long userId);

    List<ResponseBookingDTO> getUserBookings(Long userId, String status, int from, int size);

    List<ResponseBookingDTO> getUserItemsBookings(Long userId, String status, int from, int size);
}
