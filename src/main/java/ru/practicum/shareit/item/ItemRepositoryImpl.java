package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private Long id = 1L;

    @Override
    public Item addItem(Item item) {
        item = item.toBuilder()
                .id(id++)
                .build();

        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item, long userId, long itemId) {
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
    public Item getItemById(long itemId) {
        if (items.get(itemId) == null) {
            throw new IllegalArgumentException("Товар с таким айди не существует");
        }
        return items.get(itemId);
    }

    @Override
    public List<Item> getItems(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemsSearch(String text) {
        List<Item> searchItems = new ArrayList<>();
        if (text.equals("null")) {
            return searchItems;
        }
        for (Item item : items.values()) {
            if ((item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toUpperCase().contains(text.toUpperCase())) && item.getAvailable()) {
                searchItems.add(item);
            }
        }
        return searchItems;
    }
}
