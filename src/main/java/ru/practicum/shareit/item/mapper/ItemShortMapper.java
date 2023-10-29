package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class ItemShortMapper {

    public static ItemShort toItemShort(Item item) {
        return ItemShort.builder()
                .id(item.getId())
                .description(item.getDescription())
                .name(item.getName())
                .available(item.getAvailable())
                .build();
    }

    public static Item toItem(ItemShort itemShort, User user) {
        return Item.builder()
                .name(itemShort.getName())
                .description(itemShort.getDescription())
                .available(itemShort.getAvailable())
                .owner(user)
                .build();
    }
}
