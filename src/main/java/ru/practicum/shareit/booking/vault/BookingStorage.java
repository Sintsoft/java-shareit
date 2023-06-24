package ru.practicum.shareit.booking.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInvalidEntity;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItSQLException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingStorage {

    @Autowired
    private final BookingRepository repository;

    public Booking createBooking(Booking booking) {
        if (booking.getId() != null) {
            throw new ShareItInvalidEntity("Invaild booking for creation");
        }
        return saveToRepo(booking);
    }

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
        Optional<Booking> optionalBooking = repository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new ShareItEntityNotFound("User with id = " + bookingId + " not found");
        }
        return optionalBooking.get();
    }

    @Transactional
    private Booking saveToRepo(Booking booking) {
        try {
            if (booking.getStart().isAfter(booking.getEnd())
                    || booking.getStart().isEqual(booking.getEnd())
                    || booking.getStart().isBefore(LocalDateTime.now())) {
                throw new ShareItInvalidEntity("Set coorrect time");
            }
            return repository.save(booking);
        } catch (DataIntegrityViolationException ex) {
            log.debug("We got SQL error");
            throw new ShareItSQLException("Something bad happened, We are working to fix it.");
        }
    }
}
