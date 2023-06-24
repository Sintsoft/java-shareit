package ru.practicum.shareit.booking.vault;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b where b.booker = ?1 order by b.start DESC")
    List<Booking> getUserBookings(User booker);

    @Query("select b from Booking b where b.item.user = ?1 order by b.start DESC")
    List<Booking> getUserItemsBookings(User booker);
}
