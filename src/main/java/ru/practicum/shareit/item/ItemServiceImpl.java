package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.mapper.ItemShortMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemShort addItem(ItemShort itemShort, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User Not Found"));
        Item item = ItemShortMapper.toItem(itemShort, user);

        return ItemShortMapper.toItemShort(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemShort updateItem(ItemShort itemShort, long userId, long itemId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
        Item oldItem = itemRepository.findById(itemId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not Found"));
        Item item = ItemShortMapper.toItem(itemShort, user);
        item = item.toBuilder()
                .id(itemId)
                .build();

        if (item.getName() == null || item.getName().isBlank()) {
            item = item.toBuilder()
                    .name(oldItem.getName())
                    .build();
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            item = item.toBuilder()
                    .description(oldItem.getDescription())
                    .build();
        }
        if (item.getAvailable() == null) {
            item = item.toBuilder()
                    .available(oldItem.getAvailable())
                    .build();
        }

        return ItemShortMapper.toItemShort(itemRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItemById(long itemId, long userId) {
        Item item = itemRepository.getItemById(itemId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Not Found"));
        List<Comment> itemComments = commentRepository.findCommentsByItem(item);
        List<Booking> itemBookings = bookingRepository.getBookingsByItem(item);

        return ItemDtoMapper.toItemWithBookingDto(item, itemBookings, itemComments, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItems(long userId) {
        List<Item> itemsOwner = itemRepository.findItemsByOwnerId(userId);
        List<Comment> itemsComments = commentRepository.findCommentsByItems(itemsOwner);
        List<Booking> itemsBookings = bookingRepository.findBookingsByItems(itemsOwner);
        List<ItemDto> itemWithBooking = new ArrayList<>();

        for (Item item : itemsOwner) {
            List<Comment> itemComments = itemsComments.stream()
                    .filter(comment -> comment.getItem().equals(item))
                    .collect(Collectors.toList());
            List<Booking> itemBookings = itemsBookings.stream()
                    .filter(booking -> booking.getItem().equals(item))
                    .collect(Collectors.toList());
            ItemDto itemDto = ItemDtoMapper.toItemWithBookingDto(item,
                    itemBookings, itemComments, userId);

            itemWithBooking.add(itemDto);
        }

        return itemWithBooking;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemShort> getItemsSearch(String text) {
        return itemRepository.getItemsSearch(text).stream()
                .filter(Item::getAvailable)
                .map(ItemShortMapper::toItemShort)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(long itemId, long userId, Comment comment) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Not Found"));
        List<Booking> bookingList = bookingRepository.getBookingsByItem(item).stream()
                .filter(booking -> booking.getStartTime().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        if (bookingList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item Is Not Booker");
        }
        for (Booking booking : bookingList) {
            if (booking.getBooker().getId() == userId) {
                break;
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Is Not Booker");
        }
        comment = comment.toBuilder()
                .created(LocalDateTime.now())
                .user(userRepository.findById(userId).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found")))
                .item(item)
                .build();

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }
}
