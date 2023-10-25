package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByBookerIdAndEndTimeBefore
            (long bookerId, LocalDateTime localDateTime);//PAST

    List<Booking> findBookingsByBookerIdAndStartTimeAfter
            (long bookerId, LocalDateTime localDateTime);//FUTURE

    List<Booking> findBookingsByBookerIdAndStatusEquals(long bookerId, StatusBooking statusBooking);//WAITING and REJECTION

    List<Booking> findBookingsByBookerId(long bookerId);

    @Query("SELECT b FROM Booking AS b WHERE b.startTime < ?1 AND b.endTime > ?1 AND b.booker = ?2")
    List<Booking> findBookingsByBookerAndCurrent
            (LocalDateTime localDateTime, User booker);//CURRENT

    List<Booking> getBookingsByItem(Item item);
}
