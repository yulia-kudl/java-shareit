package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item addItem(long ownerId, Item item);

    long getOwner(long itemId);

    Item updateItem(Item item, long itemId);

    Item getItem(long itemId);

    List<Item> getUserItems(long ownerId);

    List<Item> searchItems(String text);
}
