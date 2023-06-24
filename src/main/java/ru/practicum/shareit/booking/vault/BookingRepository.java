package ru.practicum.shareit.booking.vault;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b where b.booker = ?1 order by b.start DESC")
    List<Booking> getUserBookings(User booker);

    @Query("select b from Booking b where b.item.user = ?1 order by b.start DESC")
    List<Booking> getUserItemsBookings(User booker);

    @Query(value = "select b.* from shareit.bookings b where b.item_id = ?1 " +
            "and start_date < now()  order by b.start_date DESC LIMIT 1",
            nativeQuery = true)
    List<Booking> getItemLastBooking(long itemId);

    @Query(value = "select b.* from shareit.bookings b where b.item_id = ?1 " +
            "and start_date > now() order by b.start_date ASC LIMIT 1",
            nativeQuery = true)
    List<Booking> getItemNextBooking(long itemId);
}
