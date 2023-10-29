package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {

    BookingDto postBooking(BookingShort bookingShort, long userId);

    BookingDto patchBooking(long bookingId, boolean available, long userId);

    BookingDto getBooking(long bookingId, long userId);

    List<BookingDto> getBookingByBooker(long userId, State state);

    List<BookingDto> getBookingItemsByOwner(long userId, State state);
}
