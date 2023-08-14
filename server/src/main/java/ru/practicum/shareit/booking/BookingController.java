package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.RequestBookingDTO;
import ru.practicum.shareit.booking.dto.ResponseBookingDTO;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    @Autowired
    private final BookingService service;

    @PostMapping
    public ResponseBookingDTO postBooking(@Validated @RequestBody RequestBookingDTO dto,
                                          @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.createBooking(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseBookingDTO patchApprove(@PathVariable long bookingId,
                                           @RequestParam boolean approved,
                                           @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.approveBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseBookingDTO getBooking(@PathVariable long bookingId,
                                         @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<ResponseBookingDTO> getUserBookings(
            @RequestParam(defaultValue = "ALL") String state,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return service.getUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<ResponseBookingDTO> getUserItemsBookings(
            @RequestParam(defaultValue = "ALL") String state,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return service.getUserItemsBookings(userId, state, from, size);
    }
}
