package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final UserRepository userRepository;
    private final Map<Long, Item> items = new HashMap<>();
    private Long id = 1L;

    @Override
    public Item addItem(ItemDto itemDto, long userId) {
        userRepository.getUserById(userId);

        itemDto = itemDto.toBuilder()
                .id(id++)
                .build();
        Item item = ItemMapper.toItem(itemDto, userId);

        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item, long userId, long itemId) {
        userRepository.getUserById(userId);
        if (items.get(itemId) == null || items.get(itemId).getOwner() != userId) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Or Item Not Found");
        }
        Item oldItem = items.get(itemId);

        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            oldItem.setDescription(item.getDescription());
        }
        items.put(itemId, oldItem);
        return oldItem;
    }

    @Override
    public ItemDto getItemById(long itemId) {
        if (items.get(itemId) == null) {
            throw new IllegalArgumentException();
        }
        return ItemMapper.toItemDto(items.get(itemId));
    }

    @Override
    public List<Item> getItems(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsSearch(String text) {
        List<ItemDto> searchItems = new ArrayList<>();
        if (text.equals("null")) {
            return searchItems;
        }
        for (Item item : items.values()) {
            if ((item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toUpperCase().contains(text.toUpperCase())) && item.getAvailable()) {
                searchItems.add(ItemMapper.toItemDto(item));
            }
        }
        return searchItems;
    }
}
