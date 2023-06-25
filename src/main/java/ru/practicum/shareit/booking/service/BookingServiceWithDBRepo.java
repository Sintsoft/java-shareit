package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequestStatus;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.vault.BookingStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.vault.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.vault.UserStorage;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInnerException;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInvalidEntity;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BookingServiceWithDBRepo implements BookingService {

    @Autowired
    private final BookingStorage bookingStorage;

    @Autowired
    private final UserStorage userStorage;

    @Autowired
    private final ItemStorage itemStorage;


    @Override
    public ResponseBookingDto createBooking(RequestBookingDto dto, Long userId) {
        try {
            Item bookedItem = itemStorage.loadItem(dto.getItemId());
            if (!bookedItem.getAvailable()) {
                throw new ShareItInvalidEntity("Can't book inavaliable item");
            }
            User booker = userStorage.loadUser(userId);
            if (booker.getId() == bookedItem.getId()) {
                throw new ShareItEntityNotFound("Owner can't book own item");
            }
            return BookingMapper.toDto(
                    bookingStorage.createBooking(
                            BookingMapper.fromDto(dto, bookedItem, booker)));
        } catch (NullPointerException ex) {
            throw new ShareItInvalidEntity("Got null pointer");
        }
    }

    @Override
    public ResponseBookingDto approveBooking(long bookingId, boolean approved, long userId) {
        try {
            Booking approvedBooking = bookingStorage.loadBooking(bookingId);
            if (itemStorage.loadItem(approvedBooking.getItem().getId()).getUser().getId() != userId) {
                throw new ShareItEntityNotFound("This user can't approve this booking");
            }
            if (approved &&
                    !(
                        approvedBooking.getStatus().equals(BookingStatus.APPROVED)
                        || approvedBooking.getStatus().equals(BookingStatus.CANCELED)
                    )
            ) {
                approvedBooking.setStatus(BookingStatus.APPROVED);
            } else if (!approved && !approvedBooking.getStatus().equals(BookingStatus.CANCELED)) {
                approvedBooking.setStatus(BookingStatus.REJECTED);
            } else {
                throw new ShareItInvalidEntity("Invalid request");
            }
            return BookingMapper.toDto(bookingStorage.updateBooking(approvedBooking));
        } catch (NullPointerException ex) {
            throw new ShareItInvalidEntity("Got null pointer");
        }
    }

    @Override
    public ResponseBookingDto getBooking(Long bookingId, Long userId) {
        Booking bookIng = bookingStorage.loadBooking(bookingId);
        if (bookIng.getBooker().getId() != userId && bookIng.getItem().getUser().getId() != userId) {
            throw new ShareItEntityNotFound("Сan't find this booking");
        }
        return BookingMapper.toDto(bookIng);
    }

    @Override
    public List<ResponseBookingDto> getUserBookings(Long userId, String stringStatus) {
        try {
            BookingRequestStatus status = BookingRequestStatus.valueOf(stringStatus);
            return bookingStorage.loadUserBookings(
                userStorage.loadUser(userId), status)
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
        } catch (IllegalArgumentException ex) {
            throw new ShareItInnerException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<ResponseBookingDto> getUserItemsBookings(Long userId, String stringStatus) {
        try {
            BookingRequestStatus status = BookingRequestStatus.valueOf(stringStatus);
            return bookingStorage.loadUserItemsBookings(
                            userStorage.loadUser(userId), status)
                    .map(BookingMapper::toDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException ex) {
            throw new ShareItInnerException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
