package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.vault.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.vault.BookingStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.vault.ItemStorage;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.vault.UserStorage;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInvalidEntity;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItNotAllowedAction;

import java.time.LocalDateTime;
import java.util.Optional;

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
    public BookingDto createBooking(BookingDto dto, Long userId) {
        try {
            Item bookedItem = itemStorage.loadItem(dto.getItemId());
            if (!bookedItem.getAvailable()) {
                throw new ShareItInvalidEntity("Can't book inavaliable item");
            }
            User booker = userStorage.loadUser(userId);
            return BookingMapper.toDto(
                    bookingStorage.createBooking(
                            BookingMapper.fromDto(dto, bookedItem, booker)));
        } catch (NullPointerException ex) {
            throw new ShareItInvalidEntity("Got null pointer");
        }
    }

    @Override
    public BookingDto approveBooking(long bookingId, boolean approved, long userId) {
        try {
            Booking approvedBooking = bookingStorage.loadBooking(bookingId);
            if (itemStorage.loadItem(approvedBooking.getItem().getId()).getUser().getId() != userId) {
                throw new ShareItNotAllowedAction("This user can't approve this booking");
            }
            if (approved &&
                    !(
                        approvedBooking.getStatus().equals(BookingStatus.APPROVED)
                        || approvedBooking.getStatus().equals(BookingStatus.CANCELED)
                    )
            ) {
                approvedBooking.setStatus(BookingStatus.APPROVED);
            } else if (!approved && !approvedBooking.getStatus().equals(BookingStatus.CANCELED)){
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
        return null; // bookingStorage.loadBooking(bookingId);
    }

}
