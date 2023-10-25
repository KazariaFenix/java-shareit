package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;

public class BookingShortMapper {

    public static Booking toBooking(BookingShort bookingShort) {
        return Booking.builder()
                .startTime(bookingShort.getStart())
                .endTime(bookingShort.getEnd())
                .build();
    }
}
