package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.mapper.BookingShortMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDto postBooking(BookingShort bookingShort, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
        Item item = itemRepository.findById(bookingShort.getItemId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Not Found"));

        if (item.getOwner().getId() == userId) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner Cannot Be Booker");
        }
        if (bookingShort.getEnd().isBefore(bookingShort.getStart()) ||
                bookingShort.getEnd().isEqual(bookingShort.getStart()) || !item.getAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "EndTime, StartTime or Available");
        }
        Booking booking = BookingShortMapper.toBooking(bookingShort);
        booking = booking.toBuilder()
                .booker(user)
                .item(item)
                .status(StatusBooking.WAITING)
                .build();

        return BookingDtoMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto patchBooking(long bookingId, boolean approved, long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking Not Found"));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Is Not Owner");
        }
        if (booking.getStatus() == StatusBooking.APPROVED && approved) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status Already Confirmed");
        }
        if (approved) {
            booking = booking.toBuilder()
                    .status(StatusBooking.APPROVED)
                    .build();
        } else {
            booking = booking.toBuilder()
                    .status(StatusBooking.REJECTED)
                    .build();
        }

        return BookingDtoMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getBooking(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking Not Found"));
        if (booking.getItem().getOwner().getId() != userId &&
                booking.getBooker().getId() != userId) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Is Not Booker Or Owner");
        }

        return BookingDtoMapper.toBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingByBooker(long userId, State state) {
        User booker = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
        List<Booking> list;
        switch (state) {
            case CURRENT:
                list = bookingRepository.findBookingsByBookerAndCurrent(LocalDateTime.now(), booker);
                break;
            case PAST:
                list = bookingRepository.findBookingsByBookerIdAndEndTimeBefore(userId, LocalDateTime.now());
                break;
            case FUTURE:
                list = bookingRepository.findBookingsByBookerIdAndStartTimeAfter(userId, LocalDateTime.now());
                break;
            case REJECTED:
                list = bookingRepository.findBookingsByBookerIdAndStatusEquals(userId, StatusBooking.REJECTED);
                break;
            case WAITING:
                list = bookingRepository.findBookingsByBookerIdAndStatusEquals(userId, StatusBooking.WAITING);
                break;
            case ALL:
                list = bookingRepository.findBookingsByBookerId(userId);
                break;
            default:
                state = State.UNSUPPORTED_STATUS;
                throw new UnsupportedStatusException("Unknown state: " + state);
        }

        return list.stream()
                .map(BookingDtoMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingItemsByOwner(long userId, State state) {
        userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
        List<Item> items = itemRepository.findItemsByOwnerId(userId);
        List<Booking> ownerBooking = new ArrayList<>();
        List<Booking> list;
        items.stream()
                .map(item -> bookingRepository.getBookingsByItem(item))
                .forEach(ownerBooking::addAll);

        switch (state) {
            case CURRENT:
                list = ownerBooking.stream()
                        .filter(booking -> LocalDateTime.now().isAfter(booking.getStartTime()))
                        .filter(booking -> LocalDateTime.now().isBefore(booking.getEndTime()))
                        .collect(Collectors.toList());
                break;
            case PAST:
                list = ownerBooking.stream()
                        .filter(booking -> LocalDateTime.now().isAfter(booking.getEndTime()))
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                list = ownerBooking.stream()
                        .filter(booking -> LocalDateTime.now().isBefore(booking.getStartTime()))
                        .collect(Collectors.toList());
                break;
            case REJECTED:
                list = ownerBooking.stream()
                        .filter(booking -> StatusBooking.REJECTED.equals(booking.getStatus()))
                        .collect(Collectors.toList());
                break;
            case WAITING:
                list = ownerBooking.stream()
                        .filter(booking -> StatusBooking.WAITING.equals(booking.getStatus()))
                        .collect(Collectors.toList());
                break;
            case ALL:
                list = ownerBooking;
                break;
            default:
                state = State.UNSUPPORTED_STATUS;
                throw new UnsupportedStatusException("Unknown state: " + state);
        }

        return list.stream()
                .map(BookingDtoMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .collect(Collectors.toList());
    }
}
