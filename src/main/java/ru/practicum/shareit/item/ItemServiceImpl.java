package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        userRepository.getUserById(userId);

        Item item = ItemMapper.toItem(itemDto, userId);
        return ItemMapper.toItemDto(itemRepository.addItem(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long userId, long itemId) {
        userRepository.getUserById(userId);

        Item item = ItemMapper.toItem(itemDto, userId);
        return ItemMapper.toItemDto(itemRepository.updateItem(item, userId, itemId));
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return ItemMapper.toItemDto(itemRepository.getItemById(itemId));
    }

    @Override
    public List<Item> getItems(long userId) {
        return itemRepository.getItems(userId);
    }

    @Override
    public List<ItemDto> getItemsSearch(String text) {
        return itemRepository.getItemsSearch(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
