package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Item addItem(ItemDto itemDto, long userId) {
        return itemRepository.addItem(itemDto, userId);
    }

    @Override
    public Item updateItem(Item item, long userId, long itemId) {
        return itemRepository.updateItem(item, userId, itemId);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public List<Item> getItems(long userId) {
        return itemRepository.getItems(userId);
    }

    @Override
    public List<ItemDto> getItemsSearch(String text) {
        return itemRepository.getItemsSearch(text);
    }
}
