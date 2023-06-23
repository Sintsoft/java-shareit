package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

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
    public BookingDto postBooking(@Validated @RequestBody BookingDto dto,
                                  @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.createBooking(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto patchApprove(@PathVariable long bookingId,
                                   @RequestParam boolean approved,
                                   @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.approveBooking(bookingId, approved, userId);
    }
}
