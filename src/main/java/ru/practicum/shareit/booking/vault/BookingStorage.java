package ru.practicum.shareit.booking.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequestStatus;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInvalidEntity;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItSQLException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingStorage {

    @Autowired
    private final BookingRepository repository;

    @Transactional
    public Booking createBooking(Booking booking) {
        if (booking.getId() != null) {
            throw new ShareItInvalidEntity("Invaild booking for creation");
        }
        return saveToRepo(booking);
    }

    @Transactional
    public Booking updateBooking(Booking booking) {
        if (booking.getId() == null) {
            throw new ShareItInvalidEntity("Invaild booking for creation");
        }
        return saveToRepo(booking);
    }

    @Transactional
    public Booking loadBooking(Long bookingId) {
        if (bookingId == null) {
            log.debug("User id can not be null");
            throw new ShareItInvalidEntity("Set item owner in header");
        }
        return repository.findById(bookingId).orElseThrow(() -> {
            throw new ShareItEntityNotFound("User with id = " + bookingId + " not found");
        });
    }

    @Transactional
    public Stream<Booking> loadUserBookings(User user, BookingRequestStatus status, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new ShareItInvalidEntity("Bad request params");
        }
        Stream<Booking> bookings = repository.getUserBookings(user.getId(), from, size)
                .stream()
                .filter(booking -> filterBookings(booking, status));
        return bookings;
    }

    @Transactional
    public Stream<Booking> loadUserItemsBookings(User user, BookingRequestStatus status, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new ShareItInvalidEntity("Bad request params");
        }
        return repository.getUserItemsBookings(user, PageRequest.of(from, size))
                .stream()
                .filter(booking -> filterBookings(booking, status));
    }

    @Transactional
    public Booking loadItemLastBooking(Item item) {
        List<Booking> lastBookingList = repository.getItemLastBooking(item.getId());
        if (lastBookingList.isEmpty()) {
            return null;
        } else if (lastBookingList.size() > 1) {
            throw new ShareItSQLException("Too much last items");
        }
        return lastBookingList.get(0);
    }

    @Transactional
    public Booking loadItemNextBooking(Item item) {
        List<Booking> lastBookingList = repository.getItemNextBooking(item.getId());
        if (lastBookingList.isEmpty()) {
            return null;
        } else if (lastBookingList.size() > 1) {
            throw new ShareItSQLException("Too much last items");
        }
        return lastBookingList.get(0);
    }

    @Transactional
    private Booking saveToRepo(Booking booking) {
        if (
                booking.getStart() == null || booking.getEnd() == null
                        || booking.getStart().isAfter(booking.getEnd())
                        || booking.getStart().isEqual(booking.getEnd())
                        || booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ShareItInvalidEntity("Set coorrect time");
        }
        try {
            return repository.save(booking);
        } catch (DataIntegrityViolationException ex) {
            log.debug("We got SQL error");
            throw new ShareItSQLException("Something bad happened, We are working to fix it.");
        }
    }

    private boolean filterBookings(Booking booking, BookingRequestStatus status) {
        switch (status) {
            case ALL:
                return true;
            case PAST:
                return booking.isPast() && booking.getStatus().equals(BookingStatus.APPROVED);
            case FUTURE:
                return  booking.isFuture()
                        && List.of(BookingStatus.APPROVED, BookingStatus.WAITING).contains(booking.getStatus());
            case CURRENT:
                return booking.isCurrent() && !booking.getStatus().equals(BookingStatus.CANCELED);
            case WAITING:
                return booking.getStatus().equals(BookingStatus.WAITING);
            case REJECTED:
                return booking.getStatus().equals(BookingStatus.REJECTED);
            default: return false;
        }
    }
}
