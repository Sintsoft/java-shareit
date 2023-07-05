package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.vault.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.vault.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.vault.UserRepository;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItEntityNotFound;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
public class BookingServiceTests {

    @Autowired
    BookingService testBookingService;

    @MockBean
    BookingRepository mockBookingRepo;

    @MockBean
    ItemRepository mockItemRepo;

    @MockBean
    UserRepository mockUserRepo;

    private RequestBookingDto testInputDTO;

    private User owner = new User(1L, "user", "email@emnail.com");
    private User booker = new User(2L, "user2", "email2@emnail.com");
    private Item bookedItem = new Item(
            1L,
            owner,
            "item",
            "description",
            true,
            null
    );

    private LocalDateTime testBookingStart = LocalDateTime.now().plusDays(1);
    private LocalDateTime testBookingEnd = LocalDateTime.now().plusDays(1).plusHours(1);

    @BeforeEach
    void setUp() {

        testInputDTO = new RequestBookingDto(
                1L,
                testBookingStart,
                testBookingEnd
        );

        Booking ownerBooking = new Booking(
                1L,
                bookedItem,
                testBookingStart,
                testBookingEnd,
                booker,
                BookingStatus.WAITING);

        Booking anotherOwnerBooking = new Booking(
                2L,
                bookedItem,
                testBookingStart,
                testBookingEnd,
                booker,
                BookingStatus.WAITING);

        when(mockUserRepo.findById(1L))
                .thenReturn(Optional.of(owner));

        when(mockUserRepo.findById(2L))
                .thenReturn(Optional.of(booker));

        when(mockItemRepo.findById(1L))
                .thenReturn(Optional.of(bookedItem));

        when(mockBookingRepo.save(
                new Booking(null, bookedItem, testBookingStart, testBookingEnd, booker, BookingStatus.WAITING)))
                .thenReturn(ownerBooking);

        when(mockBookingRepo.save(
                new Booking(1L, bookedItem, testBookingStart, testBookingEnd, booker, any())))
                .thenReturn(ownerBooking);

        when(mockItemRepo.findUserItems(owner))
                .thenReturn(List.of(bookedItem));

        when(mockBookingRepo.findById(1L))
                .thenReturn(Optional.of(ownerBooking));

        when(mockBookingRepo.getUserBookings(2L, 0, 10))
                .thenReturn(List.of(ownerBooking, anotherOwnerBooking));

        when(mockBookingRepo.getUserItemsBookings(owner, PageRequest.of(0, 10)))
                .thenReturn(List.of(ownerBooking, anotherOwnerBooking));

        when(mockBookingRepo.getItemLastBooking(1L)).thenReturn(List.of(ownerBooking));
        when(mockBookingRepo.getItemNextBooking(1L)).thenReturn(List.of(anotherOwnerBooking));

    }

    @Test
    void createBookingNormalTest() {

        testBookingService.createBooking(testInputDTO, 2L);

        verify(mockUserRepo, times(1)).findById(anyLong());
        verify(mockItemRepo, times(1)).findById(anyLong());
        verify(mockBookingRepo, times(1)).save(
                new Booking(null, bookedItem, testBookingStart, testBookingEnd, booker, BookingStatus.WAITING));
    }

    @Test
    void updateBookingTest() {

        ResponseBookingDto testDTO = testBookingService.approveBooking(1L, false, 1L);

        assertNotEquals("WAITING", testDTO.getStatus());
        verify(mockBookingRepo, times(1)).save(
                new Booking(1L, bookedItem, testBookingStart, testBookingEnd, booker, BookingStatus.REJECTED));

        testDTO = testBookingService.approveBooking(1L, true, 1L);

        assertNotEquals("WAITING", testDTO.getStatus());
        verify(mockBookingRepo, times(2)).save(
                new Booking(1L, bookedItem, testBookingStart, testBookingEnd, booker, BookingStatus.APPROVED));
    }

    @Test
    void loadBookingWithWrongUserTest() {
        assertThrows(ShareItEntityNotFound.class, () -> {
            testBookingService.approveBooking(1L, false, 5L); }
        );
    }

    @Test
    void loadUserBookingsTest() {
        List<ResponseBookingDto> testBookings
                = testBookingService.getUserBookings(2L, "ALL", 0, 10);

        assertEquals(2, testBookings.size());
        verify(mockBookingRepo, times(1)).getUserBookings(booker.getId(), 0, 10);
    }

    @Test
    void loadUserItemsBookingsTest() {
        List<ResponseBookingDto> testBookings
                = testBookingService.getUserItemsBookings(1L, "ALL", 0, 10);

        assertEquals(2, testBookings.size());
        verify(mockBookingRepo, times(1)).getUserItemsBookings(owner, PageRequest.of(0, 10));
    }
}
