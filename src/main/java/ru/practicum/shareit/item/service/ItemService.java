package ru.practicum.shareit.item.service;

import jakarta.validation.Valid;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(long ownerId, Item item);

    Item updateItem(long itemId, long ownerId, Item item);

    Item getItem(long itemId,long userId);

    List<Item> getUserItems(long ownerId);

    List<Item> searchItems(String text);

    Comment addComment(long userId, long itemId, Comment comment);
}
