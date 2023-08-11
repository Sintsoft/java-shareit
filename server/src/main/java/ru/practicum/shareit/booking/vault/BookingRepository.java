package ru.practicum.shareit.booking.vault;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select b.* from bookings b where b.item_id = :itemId " +
            "and start_date < now() AND status = 'APPROVED' order by b.start_date DESC FETCH FIRST 1 ROW ONLY",
            nativeQuery = true)
    List<Booking> getItemLastBooking(@Param("itemId") long itemId);

    @Query(value = "select b.* from bookings b where b.item_id = :itemId " +
            "and start_date > now() AND status NOT IN ('REJECTED', 'CANCELED') " +
            "order by b.start_date ASC FETCH FIRST 1 ROW ONLY",
            nativeQuery = true)
    List<Booking> getItemNextBooking(@Param("itemId") long itemId);

    @Query(nativeQuery = true,
           value = "select b.* from bookings b where B.item_id = :itemId " +
                   "order by b.start desc offset :from rows fetch first :size rows only")
    List<Booking> getItemBookings(@Param("itemId") long itemId,
                                  @Param("from") int from,
                                  @Param("size") int size);


    @Query(nativeQuery = true,
           value = "select b.* from bookings b where B.booker_id = :userId " +
                    "order by b.start desc offset :from rows fetch first :size rows only")
    List<Booking> getUserBookings(@Param("userId") long userId,
                                  @Param("from") int from,
                                  @Param("size") int size);

    // По наличию элементов в этом списке проверим брал ли пользователь вещь (для коммента)
    @Query(nativeQuery = true,
           value = "select b.* from bookings b where b.item_id = :itemId " +
                    "and b.booker_id = :userId and b.end_date < now()")
    List<Booking> getUseкPastBookingsOfItem(@Param("itemId") Long itemId, @Param("userId") Long userId);

    /*
     * User items bookings
     * */

    // Status = ALL
    @Query(nativeQuery = true,
            value = "select b.* from bookings b left join items i on b.item_id = i.id where i.user_id = :userId " +
                    "order by b.start desc offset :from rows fetch first :size rows only")
    List<Booking> getAllUserItemsBookings(@Param("userId") Long userId,
                                     @Param("from") int from,
                                     @Param("size") int size);

    // Status = PAST
    @Query(nativeQuery = true,
            value = "select b.* from bookings b left join items i on b.item_id = i.id where i.user_id = :userId " +
                    "and b.end_date < now() and b.status = 'APPROVED' " +
                    "order by b.start_date  desc offset :from rows fetch first :size rows only")
    List<Booking> getPastUseItemsrBookings(@Param("userId") Long userId,
                                     @Param("from") int from,
                                     @Param("size") int size);

    // Status = FUTURE
    @Query(nativeQuery = true,
            value = "select b.* from bookings b left join items i on b.item_id = i.id where i.user_id = :userId " +
                    "and b.start_date > now() and b.status in ('APPROVED', 'WAITING') " +
                    "order by b.start_date  desc offset :from rows fetch first :size rows only")
    List<Booking> getFutureUserItemsBookings(@Param("userId") Long userId,
                                      @Param("from") int from,
                                      @Param("size") int size);

    // Status = FUTURE
    @Query(nativeQuery = true,
            value = "select b.* from bookings b left join items i on b.item_id = i.id where i.user_id = :userId " +
                    "and (b.end_date > now() and b.start_date < now()) and b.status not in ('CANCELED', 'REJECTED') " +
                    "order by b.start_date  desc offset :from rows fetch first :size rows only")
    List<Booking> getCurrentUserItemsBookings(@Param("userId") Long userId,
                                        @Param("from") int from,
                                        @Param("size") int size);

    // Status = WAITING OR REJECTED
    @Query(nativeQuery = true,
            value = "select b.* from bookings b left join items i on b.item_id = i.id where i.user_id = :userId " +
                    "and b.status = :status " +
                    "order by b.start_date  desc offset :from rows fetch first :size rows only")
    List<Booking> getStatusUserItemsBookings(@Param("userId") Long userId,
                                              @Param("status") String status,
                                              @Param("from") int from,
                                              @Param("size") int size);

    /*
     * User Bookings block
     * */

    // Status = ALL
    @Query(nativeQuery = true,
            value = "select b.* from bookings b where b.booker_id = :userId " +
                    "order by b.start_date desc offset :from rows fetch first :size rows only")
    List<Booking> getAllUserBookings(@Param("userId") Long userId,
                                          @Param("from") int from,
                                          @Param("size") int size);

    // Status = PAST
    @Query(nativeQuery = true,
            value = "select b.* from bookings b where b.booker_id = :userId " +
                    "and b.end_date < now() and b.status = 'APPROVED' " +
                    "order by b.start_date  desc offset :from rows fetch first :size rows only")
    List<Booking> getPastUserBookings(@Param("userId") Long userId,
                                           @Param("from") int from,
                                           @Param("size") int size);

    // Status = FUTURE
    @Query(nativeQuery = true,
            value = "select b.* from bookings b where b.booker_id = :userId " +
                    "and b.start_date > now() and b.status in ('APPROVED', 'WAITING') " +
                    "order by b.start_date  desc offset :from rows fetch first :size rows only")
    List<Booking> getFutureUserBookings(@Param("userId") Long userId,
                                             @Param("from") int from,
                                             @Param("size") int size);

    // Status = FUTURE
    @Query(nativeQuery = true,
            value = "select b.* from bookings b where b.booker_id = :userId " +
                    "and (b.end_date > now() and b.start_date < now()) and b.status not in ('CANCELED', 'REJECTED') " +
                    "order by b.start_date  desc offset :from rows fetch first :size rows only")
    List<Booking> getCurrentUserBookings(@Param("userId") Long userId,
                                              @Param("from") int from,
                                              @Param("size") int size);

    // Status = WAITING OR REJECTED
    @Query(nativeQuery = true,
            value = "select b.* from bookings b where b.booker_id = :userId " +
                    "and b.status = :status " +
                    "order by b.start_date  desc offset :from rows fetch first :size rows only")
    List<Booking> getStatusUserBookings(@Param("userId") Long userId,
                                             @Param("status") String status,
                                             @Param("from") int from,
                                             @Param("size") int size);
}
