package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
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
    private final BookingRepository bookingRepo;

    @Autowired
    private final UserService userService;

    @Autowired
    private final ItemService itemService;


    @Override
    public BookingDto createBooking(BookingDto dto, Long userId) {
        try {
            if (dto.getStart().isAfter(dto.getEnd())
                    || dto.getStart().isEqual(dto.getEnd())
                    || dto.getStart().isBefore(LocalDateTime.now())) {
                throw new ShareItInvalidEntity("Set coorrect time");
            }
            Item bookedItem = itemService.loadItem(dto.getItemId());
            if (!bookedItem.getAvailable()) {
                throw new ShareItInvalidEntity("Can't book inavaliable item");
            }
            User booker = userService.loadUser(userId);
            Booking newBooking = BookingMapper.fromDto(dto, bookedItem, booker);
            newBooking = bookingRepo.save(newBooking);
            return BookingMapper.toDto(newBooking);
        } catch (NullPointerException ex) {
            throw new ShareItInvalidEntity("Got null pointer");
        }
    }

    @Override
    public BookingDto approveBooking(long bookingId, boolean approved, long userId) {
        try {
            Booking approvedBooking = loadBooking(bookingId);
            if (itemService.loadItem(approvedBooking.getItem().getId()).getUser().getId() != userId) {
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
            return BookingMapper.toDto(bookingRepo.save(approvedBooking));
        } catch (NullPointerException ex) {
            throw new ShareItInvalidEntity("Got null pointer");
        }
    }

    @Override
    public Booking loadBooking(long bookingId) {
        Optional<Booking> optionalBooking = bookingRepo.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new ShareItEntityNotFound("Booking not found");
        }
        return optionalBooking.get();
    }

}
