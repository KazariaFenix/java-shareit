package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingWithItemDto;
import ru.practicum.shareit.booking.mapper.BookingWithItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemDtoMapper {

    public static ItemDto toItemWithBookingDto(Item item, List<Booking> bookingList, List<Comment> itemComments,
                                               long userId) {
        List<BookingWithItemDto> bookingWithItemList = bookingList.stream()
                .filter(booking -> StatusBooking.APPROVED.equals(booking.getStatus()))
                .map(BookingWithItemMapper::toBookingWithItemDto)
                .collect(Collectors.toList());
        Optional<BookingWithItemDto> lastBooking = bookingWithItemList.stream()
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(BookingWithItemDto::getStart));
        Optional<BookingWithItemDto> nextBooking = bookingWithItemList.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(BookingWithItemDto::getStart));
        List<CommentDto> itemCommentsDto = itemComments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        if (item.getOwner().getId() != userId) {
            lastBooking = Optional.empty();
            nextBooking = Optional.empty();
        }

        return ItemDto.builder()
                .id(item.getId())
                .description(item.getDescription())
                .name(item.getName())
                .available(item.getAvailable())
                .lastBooking(lastBooking.orElse(null))
                .nextBooking(nextBooking.orElse(null))
                .comments(itemCommentsDto)
                .build();
    }
}
