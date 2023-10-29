package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ItemService {

    ItemShort addItem(ItemShort itemShort, long userId);

    ItemShort updateItem(ItemShort itemShort, long userId, long itemId);

    ItemDto getItemById(long itemId, long userId);

    List<ItemDto> getItems(long userId);

    List<ItemShort> getItemsSearch(String text);

    CommentDto addComment(long itemId, long userId, Comment comment);
}
