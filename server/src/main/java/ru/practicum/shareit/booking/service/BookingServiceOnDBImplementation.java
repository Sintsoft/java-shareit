package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.RequestBookingDTO;
import ru.practicum.shareit.booking.dto.ResponseBookingDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequestStatus;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.vault.BookingStorage;
import ru.practicum.shareit.comment.vault.CommentStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.vault.ItemStorage;
import ru.practicum.shareit.request.vault.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.vault.UserStorage;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.exceptions.ShareItInvalidEntity;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BookingServiceOnDBImplementation implements BookingService {

    @Autowired
    private final ItemRequestStorage requestStorage;

    @Autowired
    private final UserStorage userStorage;

    @Autowired
    private final ItemStorage itemStorage;

    @Autowired
    private final BookingStorage bookingStorage;

    @Autowired
    private final CommentStorage commentStorage;


    @Override
    public ResponseBookingDTO createBooking(RequestBookingDTO dto, Long userId) {
        Item item = itemStorage.getItem(dto.getItemId());
        if (!item.getAvailable()) {
            throw new ShareItInvalidEntity("Can't book inavaliable item");
        }
        User booker = userStorage.getUser(userId);
        if (item.getUser().equals(booker)) {
            throw new ShareItEntityNotFound("Owner can't book own item");
        }
        return BookingMapper.toDTO(
                bookingStorage.saveBooking(
                        BookingMapper.fromDTO(dto, booker, item)
                ));
    }

    @Override
    public ResponseBookingDTO approveBooking(long bookingId, boolean approved, long userId) {
        Booking booking = bookingStorage.getBooking(bookingId);
        if (booking.getItem().getUser().getId() != userId) {
            throw new ShareItEntityNotFound("This user can't approve this booking");
        }
        if (approved &&
                !(
                        booking.getStatus().equals(BookingStatus.APPROVED)
                                || booking.getStatus().equals(BookingStatus.CANCELED)
                )
        ) {
            booking.setStatus(BookingStatus.APPROVED);
        } else if (!approved && !booking.getStatus().equals(BookingStatus.CANCELED)) {
            booking.setStatus(BookingStatus.REJECTED);
        } else {
            throw new ShareItInvalidEntity("Invalid request");
        }
        return BookingMapper.toDTO(
                bookingStorage.saveBooking(booking)
        );
    }

    @Override
    public ResponseBookingDTO getBooking(Long bookingId, Long userId) {
        Booking bookIng = bookingStorage.getBooking(bookingId);
        if (!bookIng.getBooker().getId().equals(userId) && !bookIng.getItem().getUser().getId().equals(userId)) {
            throw new ShareItEntityNotFound("Сan't find this booking");
        }
        return BookingMapper.toDTO(bookIng);
    }

    @Override
    public List<ResponseBookingDTO> getUserBookings(Long userId, String status, int from, int size) {
        return bookingStorage.getUserBookings(
                    userStorage.getUser(userId),
                    BookingRequestStatus.valueOf(status),
                    from, size)
                .stream()
                .map(booking -> {
                    booking.setStart(null);
                    booking.setEnd(null);
                    return booking;
                })
                .map(BookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResponseBookingDTO> getUserItemsBookings(Long userId, String status, int from, int size) {
        return bookingStorage.getUserItemsBookings(
                        userStorage.getUser(userId),
                        BookingRequestStatus.valueOf(status),
                        from, size)
                .stream()
                .map(BookingMapper::toDTO)
                .collect(Collectors.toList());
    }
}
