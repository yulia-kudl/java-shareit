package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(long ownerId, Item item);

    Item updateItem(long itemId, long ownerId, Item item);

    Item getItem(long itemId);

    List<Item> getUserItems(long ownerId);

    List<Item> searchItems(String text);
}
