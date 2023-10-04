package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item addItem(ItemDto itemDto, long userId);

    Item updateItem(Item item, long userId, long itemId);

    ItemDto getItemById(long itemId);

    List<Item> getItems(long userId);

    List<ItemDto> getItemsSearch(String text);
}
