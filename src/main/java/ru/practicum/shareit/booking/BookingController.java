package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    @Autowired
    private final BookingService service;

    @PostMapping
    public ResponseBookingDto postBooking(@Validated @RequestBody RequestBookingDto dto,
                                  @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.createBooking(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseBookingDto patchApprove(@PathVariable long bookingId,
                                   @RequestParam boolean approved,
                                   @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.approveBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseBookingDto getBooking(@PathVariable long bookingId,
                                         @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<ResponseBookingDto> getUserBookings(
            @RequestParam(defaultValue = "ALL") String state,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<ResponseBookingDto> getUserItemsBookings(
            @RequestParam(defaultValue = "ALL") String state,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.getUserItemsBookings(userId, state);
    }
}
