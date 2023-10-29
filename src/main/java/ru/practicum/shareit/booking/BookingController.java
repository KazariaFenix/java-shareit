package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.State;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private BookingService bookingService;

    @PostMapping
    BookingDto postBooking(@Valid @RequestBody BookingShort bookingShort,
                           @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.postBooking(bookingShort, userId);
    }

    @PatchMapping("/{bookingId}")
    BookingDto patchBooking(@PathVariable long bookingId, @RequestParam boolean approved,
                            @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.patchBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    BookingDto getBookingById(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    List<BookingDto> getBookingByBookerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getBookingByBooker(userId, state);
    }

    @GetMapping("/owner")
    List<BookingDto> getBookingByOwnerItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getBookingItemsByOwner(userId, state);
    }
}
