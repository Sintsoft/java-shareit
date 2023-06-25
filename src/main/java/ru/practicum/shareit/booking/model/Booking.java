package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings", schema = "shareit")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "start_date")
    private LocalDateTime start;

    @Column(name = "end_date")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User booker;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    public boolean isCurrent() {
        return this.start.isBefore(LocalDateTime.now())
                && this.end.isAfter(LocalDateTime.now());
    }

    public boolean isPast() {
        return this.end.isBefore(LocalDateTime.now());
    }

    public boolean isFuture() {
        return this.start.isAfter(LocalDateTime.now());
    }
}
