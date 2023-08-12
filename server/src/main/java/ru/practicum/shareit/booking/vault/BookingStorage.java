package ru.practicum.shareit.booking.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequestStatus;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.exceptions.ShareItInvalidEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingStorage {

    @Autowired
    private final BookingRepository repository;

    @Transactional
    public Booking saveBooking(Booking booking) {
        return repository.save(booking);
    }

    @Transactional
    public Booking getBooking(Long bookingId) {
        return repository.findById(bookingId)
                .orElseThrow(
                        () -> new ShareItEntityNotFound("Booking with id = " + bookingId + " not found"));
    }

    @Transactional
    public Booking getItemLastBooking(Item item) {
        List<Booking> bookings = repository.getItemLastBooking(item.getId());
        return bookings.isEmpty() ? null : bookings.get(0);
    }

    @Transactional
    public Booking getItemNextBooking(Item item) {
        List<Booking> bookings = repository.getItemNextBooking(item.getId());
        return bookings.isEmpty() ? null : bookings.get(0);
    }

    @Transactional
    public List<Booking> getUserBookings(User user, BookingRequestStatus status, int from, int size) {
        switch (status) {
            case ALL:
                return repository.getAllUserBookings(user.getId(), from, size);
            case PAST:
                return repository.getPastUserBookings(user.getId(), from, size);
            case FUTURE:
                return repository.getFutureUserBookings(user.getId(), from, size);
            case CURRENT:
                List<Booking> tmp = repository.getCurrentUserBookings(user.getId(), from, size);
                return tmp;
            case WAITING:
                return repository.getStatusUserBookings(user.getId(), BookingStatus.WAITING.toString(), from, size);
            case REJECTED:
                return repository.getStatusUserBookings(user.getId(), BookingStatus.REJECTED.toString(), from, size);
            default:
                throw new ShareItInvalidEntity("Unknown status");
        }

    }

    @Transactional
    public List<Booking> getUserItemsBookings(User user, BookingRequestStatus status, int from, int size) {
        switch (status) {
            case ALL:
                return repository.getAllUserItemsBookings(user.getId(), from, size);// repository.getAllUserBookings(user.getId(), from, size);
            case PAST:
                return repository.getPastUserItemsBookings(user.getId(), from, size);
            case FUTURE:
                return repository.getFutureUserItemsBookings(user.getId(), from, size);
            case CURRENT:
                return repository.getCurrentUserItemsBookings(user.getId(), from, size);
            case WAITING:
                return repository.getStatusUserItemsBookings(user.getId(), BookingStatus.WAITING.toString(),  from, size);
            case REJECTED:
                return repository.getStatusUserItemsBookings(user.getId(), BookingStatus.REJECTED.toString(),  from, size);
            default:
                throw new ShareItInvalidEntity("Unknown status");
        }
    }

    @Transactional
    public boolean userNotBookedItem(User user, Item item) {
        return repository.getUserPastBookingsOfItem(
                item.getId(), user.getId()).isEmpty();
    }
}
